import { useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { api } from '../lib/api'
import { useAuth } from '../context/AuthContext'
import type { AuctionDetails, AuctionLotDetails } from '../types'
import AuctionStatusBadge from '../components/AuctionStatusBadge'
import LotStatusBadge from '../components/LotStatusBadge'
import Modal from '../components/Modal'
import Pagination from '../components/Pagination'

function formatMoney(n: number) {
  return new Intl.NumberFormat('ru-RU', { style: 'currency', currency: 'RUB', maximumFractionDigits: 0 }).format(n)
}

export default function AuctionDetailPage() {
  const { id } = useParams<{ id: string }>()
  const { user } = useAuth()
  const qc = useQueryClient()
  const [lotsPage, setLotsPage] = useState(1)
  const [bidModal, setBidModal] = useState<{ lotGuid: string; lotNumber: number; lotTitle: string; minPrice: number } | null>(null)
  const [bidAmount, setBidAmount] = useState('')
  const [bidError, setBidError] = useState('')
  const [addLotParticipant, setAddLotParticipant] = useState<{ guid: string; fullName: string } | null>(null)

  const { data: auction, isLoading } = useQuery<AuctionDetails>({
    queryKey: ['auction', id],
    queryFn: async () => {
      const { data } = await api.get(`/api/v1/auctions/${id}/`)
      return data
    },
  })

  const { data: lotsData } = useQuery({
    queryKey: ['auction-lots', id, lotsPage],
    queryFn: async () => {
      const { data } = await api.get(`/api/v1/auctions/${id}/lots/?page=${lotsPage}&count=30`)
      return data
    },
    enabled: !!id,
  })

  const actionMutation = useMutation({
    mutationFn: (action: 'start' | 'finish') => api.post(`/api/v1/auctions/${id}/${action}/`),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['auction', id] }),
  })

  const participateMutation = useMutation({
    mutationFn: () => api.post(`/api/v1/auctions/${id}/participants/participate-application/`),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['auction', id] }),
  })

  const bidMutation = useMutation({
    mutationFn: ({ lotGuid, amount }: { lotGuid: string; amount: number }) =>
      api.post(`/api/v1/auctions/${id}/lots/${lotGuid}/bids/`, { amount, participant: { guid: user?.participant?.guid } }),
    onSuccess: (data) => {
      if (data.data.status === 'ACCEPTED') {
        setBidModal(null)
        setBidAmount('')
        qc.invalidateQueries({ queryKey: ['auction', id] })
      } else {
        setBidError('Ставка не принята')
      }
    },
    onError: (err: any) => setBidError(err.response?.data?.message || 'Ошибка'),
  })

  const forSaleMutation = useMutation({
    mutationFn: (lotGuid: string) => api.post(`/api/v1/auctions/${id}/lots/${lotGuid}/for-sale/`),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['auction', id] }),
  })

  const { data: participantDetails } = useQuery({
    queryKey: ['auction-participant-details', id, addLotParticipant?.guid],
    queryFn: async () => {
      const { data } = await api.get(`/api/v1/auctions/${id}/participants/${addLotParticipant!.guid}/`)
      return data
    },
    enabled: !!addLotParticipant,
  })

  const addLotMutation = useMutation({
    mutationFn: (lotGuid: string) => api.post(`/api/v1/auctions/${id}/lots/`, { lot: lotGuid }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['auction-lots', id] })
      qc.invalidateQueries({ queryKey: ['auction', id] })
      setAddLotParticipant(null)
    },
  })

  if (isLoading) {
    return (
      <div className="max-w-7xl mx-auto px-4 py-8">
        <div className="animate-pulse space-y-4">
          <div className="h-8 bg-gray-200 rounded w-1/3" />
          <div className="h-4 bg-gray-100 rounded w-1/4" />
        </div>
      </div>
    )
  }

  if (!auction) return <div className="text-center py-20 text-gray-400">Аукцион не найден</div>

  const isAdmin = user?.role === 'ADMIN'
  const isParticipant = user?.role === 'PARTICIPANT'
  const isActive = auction.status.code === 'ACTIVE'
  const isPublished = auction.status.code === 'PUBLISHED'

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="mb-2">
        <Link to="/" className="text-sm text-blue-600 hover:underline">← Все аукционы</Link>
      </div>

      <div className="card p-6 mb-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-3 mb-1">
              <h1 className="text-3xl font-bold text-gray-900">Аукцион №{auction.number}</h1>
              <AuctionStatusBadge status={auction.status} />
            </div>
            <p className="text-gray-500">📅 {auction.startDate}</p>
          </div>
          <div className="flex flex-wrap gap-2">
            {isAdmin && isPublished && (
              <button
                onClick={() => actionMutation.mutate('start')}
                disabled={actionMutation.isPending}
                className="btn-success"
              >▶ Запустить торги</button>
            )}
            {isAdmin && isActive && (
              <button
                onClick={() => actionMutation.mutate('finish')}
                disabled={actionMutation.isPending}
                className="btn-danger"
              >⏹ Завершить торги</button>
            )}
            {isParticipant && isPublished && (
              <button
                onClick={() => participateMutation.mutate()}
                disabled={participateMutation.isPending}
                className="btn-primary"
              >Подать заявку на участие</button>
            )}
          </div>
        </div>

        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 mt-6 pt-6 border-t border-gray-100">
          <div className="text-center">
            <p className="text-2xl font-bold text-gray-900">{auction.tradingLots?.length ?? 0}</p>
            <p className="text-sm text-gray-500">Лотов</p>
          </div>
          <div className="text-center">
            <p className="text-2xl font-bold text-gray-900">{auction.participants?.length ?? 0}</p>
            <p className="text-sm text-gray-500">Участников</p>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-xl font-semibold text-gray-900">Лоты</h2>
          </div>
          {!auction.tradingLots?.length ? (
            <div className="card p-8 text-center text-gray-400">
              <div className="text-3xl mb-2">📦</div>
              <p>Лоты не добавлены</p>
            </div>
          ) : (
            <div className="space-y-3">
              {auction.tradingLots?.map((item: any) => (
                <div key={item.guid} className="card p-4">
                  <div className="flex items-start gap-4">
                    {item.photo?.[0]?.url ? (
                      <img
                        src={item.photo[0].url}
                        alt={item.title}
                        className="w-16 h-16 object-cover rounded-lg flex-shrink-0"
                      />
                    ) : (
                      <div className="w-16 h-16 bg-gray-100 rounded-lg flex items-center justify-center text-2xl flex-shrink-0">📦</div>
                    )}
                    <div className="flex-1 min-w-0">
                      <div className="flex items-start justify-between gap-2">
                        <div>
                          <h3 className="font-medium text-gray-900">{item.title}</h3>
                          {item.status && <LotStatusBadge status={item.status} />}
                        </div>
                        <div className="text-right flex-shrink-0">
                          <p className="text-sm text-gray-500">Старт</p>
                          <p className="font-semibold">{formatMoney(item.startPrice)}</p>
                          {item.lastBid && (
                            <>
                              <p className="text-xs text-gray-400 mt-1">Последняя ставка</p>
                              <p className="font-bold text-green-600">{formatMoney(item.lastBid.amount)}</p>
                            </>
                          )}
                        </div>
                      </div>
                      <div className="mt-2 flex items-center gap-2 flex-wrap">
                        {isActive && item.status === 'ON_TRADE' && isParticipant && (
                          <button
                            onClick={() => {
                              setBidModal({ lotGuid: item.guid, lotNumber: item.number, lotTitle: item.title, minPrice: item.lastBid ? item.lastBid.amount + item.startPrice : item.startPrice })
                              setBidError('')
                            }}
                            className="btn-primary text-xs px-3 py-1"
                          >Сделать ставку</button>
                        )}
                        {isAdmin && (
                          <button
                            onClick={() => forSaleMutation.mutate(item.guid)}
                            className="btn-success text-xs px-3 py-1"
                          >Выставить на продажу</button>
                        )}
                      </div>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
          <Pagination page={lotsPage} pageCount={lotsData?.page?.pageCount ?? 1} onChange={setLotsPage} />
        </div>

        <div>
          <h2 className="text-xl font-semibold text-gray-900 mb-4">Участники</h2>
          {auction.participants?.length === 0 ? (
            <div className="card p-6 text-center text-gray-400 text-sm">Нет участников</div>
          ) : (
            <div className="space-y-2">
              {auction.participants?.slice(0, 10).map((p) => (
                <div key={p.guid} className="card p-3 flex items-center gap-3">
                  <div className="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center text-blue-700 font-medium text-xs flex-shrink-0">
                    {p.fullName?.charAt(0) || '?'}
                  </div>
                  <div className="min-w-0 flex-1">
                    <p className="text-sm font-medium text-gray-900 truncate">{p.fullName}</p>
                    <p className="text-xs text-gray-400 truncate">{p.email}</p>
                  </div>
                  {isAdmin && (
                    <button
                      onClick={() => setAddLotParticipant({ guid: p.id ?? p.guid, fullName: p.fullName })}
                      className="text-xs btn-primary px-2 py-1 flex-shrink-0"
                    >+ Лот</button>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {addLotParticipant && (
        <Modal title={`Лоты участника: ${addLotParticipant.fullName}`} onClose={() => setAddLotParticipant(null)}>
          <div className="space-y-3">
            {!participantDetails ? (
              <p className="text-sm text-gray-400">Загрузка...</p>
            ) : participantDetails.availableLots?.length === 0 ? (
              <p className="text-sm text-gray-400">Нет доступных лотов</p>
            ) : (
              participantDetails.availableLots?.map((lot: any) => (
                <div key={lot.guid} className="flex items-center justify-between p-3 border border-gray-100 rounded-lg">
                  <div>
                    <p className="font-medium text-sm">{lot.title}</p>
                    <p className="text-xs text-gray-400">{formatMoney(lot.startPrice)}</p>
                  </div>
                  <button
                    onClick={() => addLotMutation.mutate(lot.guid)}
                    disabled={addLotMutation.isPending}
                    className="btn-success text-xs px-3 py-1"
                  >Добавить</button>
                </div>
              ))
            )}
            <div className="flex justify-end pt-2">
              <button onClick={() => setAddLotParticipant(null)} className="btn-secondary text-sm">Закрыть</button>
            </div>
          </div>
        </Modal>
      )}

      {bidModal && (
        <Modal title={`Ставка на "${bidModal.lotTitle}"`} onClose={() => setBidModal(null)}>
          <div className="space-y-4">
            <p className="text-sm text-gray-500">Минимальная ставка: <strong>{formatMoney(bidModal.minPrice)}</strong></p>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Ваша ставка (₽)</label>
              <input
                type="number"
                className="input"
                min={bidModal.minPrice}
                value={bidAmount}
                onChange={e => setBidAmount(e.target.value)}
              />
            </div>
            {bidError && <div className="bg-red-50 text-red-700 text-sm rounded px-3 py-2">{bidError}</div>}
            <div className="flex gap-3 justify-end">
              <button onClick={() => setBidModal(null)} className="btn-secondary">Отмена</button>
              <button
                onClick={() => bidMutation.mutate({ lotGuid: bidModal.lotGuid, amount: Number(bidAmount) })}
                disabled={bidMutation.isPending || !bidAmount}
                className="btn-primary"
              >Сделать ставку</button>
            </div>
          </div>
        </Modal>
      )}
    </div>
  )
}
