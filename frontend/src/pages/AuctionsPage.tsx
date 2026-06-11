import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { api } from '../lib/api'
import { useAuth } from '../context/AuthContext'
import type { AuctionListItem } from '../types'
import AuctionStatusBadge from '../components/AuctionStatusBadge'
import Pagination from '../components/Pagination'
import Modal from '../components/Modal'

function formatMoney(n: number) {
  return new Intl.NumberFormat('ru-RU', { style: 'currency', currency: 'RUB', maximumFractionDigits: 0 }).format(n)
}

export default function AuctionsPage() {
  const { user } = useAuth()
  const qc = useQueryClient()
  const [page, setPage] = useState(1)
  const [statusFilter, setStatusFilter] = useState<string[]>([])
  const [showCreateModal, setShowCreateModal] = useState(false)
  const [createDate, setCreateDate] = useState('')
  const [createError, setCreateError] = useState('')

  const { data, isLoading } = useQuery({
    queryKey: ['auctions', page, statusFilter],
    queryFn: async () => {
      const params = new URLSearchParams()
      params.append('page', String(page))
      params.append('count', '12')
      statusFilter.forEach(s => params.append('statuses', s))
      const { data } = await api.get(`/api/v1/auctions/?${params}`)
      return data
    },
  })

  const createMutation = useMutation({
    mutationFn: (startDate: string) => api.post('/api/v1/auctions/', { startDate }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['auctions'] })
      setShowCreateModal(false)
      setCreateDate('')
    },
    onError: (err: any) => setCreateError(err.response?.data?.message || 'Ошибка'),
  })

  const toggleStatus = (s: string) => {
    setStatusFilter(prev =>
      prev.includes(s) ? prev.filter(x => x !== s) : [...prev, s]
    )
    setPage(1)
  }

  const statuses = [
    { code: 'PUBLISHED', label: 'Опубликованные', cls: 'bg-yellow-100 text-yellow-700 border-yellow-300' },
    { code: 'ACTIVE', label: 'Активные', cls: 'bg-green-100 text-green-700 border-green-300' },
    { code: 'ARCHIVE', label: 'Архивные', cls: 'bg-gray-100 text-gray-700 border-gray-300' },
  ]

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex items-start justify-between mb-6">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Аукционы</h1>
          <p className="text-gray-500 mt-1">Торговая площадка</p>
        </div>
        {user?.role === 'ADMIN' && (
          <button onClick={() => setShowCreateModal(true)} className="btn-primary">
            + Создать аукцион
          </button>
        )}
      </div>

      <div className="flex gap-2 mb-6 flex-wrap">
        {statuses.map(s => (
          <button
            key={s.code}
            onClick={() => toggleStatus(s.code)}
            className={`px-3 py-1.5 rounded-full text-sm font-medium border transition-all ${
              statusFilter.includes(s.code)
                ? s.cls + ' shadow-sm'
                : 'bg-white text-gray-500 border-gray-200 hover:bg-gray-50'
            }`}
          >
            {s.label}
          </button>
        ))}
      </div>

      {isLoading ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {Array.from({ length: 6 }).map((_, i) => (
            <div key={i} className="card p-6 animate-pulse">
              <div className="h-4 bg-gray-200 rounded w-1/3 mb-3" />
              <div className="h-6 bg-gray-200 rounded w-1/2 mb-4" />
              <div className="h-4 bg-gray-100 rounded w-full mb-2" />
              <div className="h-4 bg-gray-100 rounded w-2/3" />
            </div>
          ))}
        </div>
      ) : data?.items?.length === 0 ? (
        <div className="text-center py-16 text-gray-400">
          <div className="text-5xl mb-4">📋</div>
          <p className="text-lg">Аукционы не найдены</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {data?.items?.map((auction: AuctionListItem) => (
            <Link
              key={auction.guid}
              to={`/auctions/${auction.guid}`}
              className="card p-6 hover:shadow-md transition-shadow block"
            >
              <div className="flex items-start justify-between mb-3">
                <div>
                  <p className="text-xs text-gray-400 font-medium uppercase tracking-wide">Аукцион</p>
                  <p className="text-2xl font-bold text-gray-900">№{auction.number}</p>
                </div>
                <AuctionStatusBadge status={auction.status} />
              </div>
              <div className="space-y-2 text-sm text-gray-600">
                <div className="flex items-center gap-2">
                  <span>📅</span>
                  <span>{auction.startDate}</span>
                </div>
                <div className="flex items-center gap-2">
                  <span>📦</span>
                  <span>{auction.lotsCount} лотов</span>
                </div>
                <div className="flex items-center gap-2">
                  <span>👥</span>
                  <span>{auction.participantCount} участников</span>
                </div>
                <div className="flex items-center gap-2">
                  <span>💰</span>
                  <span className="font-medium text-gray-800">{formatMoney(auction.budget)}</span>
                </div>
              </div>
              {auction.participation && (
                <div className="mt-3 pt-3 border-t border-gray-100">
                  <span className="badge-blue">Вы участвуете</span>
                </div>
              )}
            </Link>
          ))}
        </div>
      )}

      <Pagination
        page={page}
        pageCount={data?.page?.pageCount ?? 1}
        onChange={setPage}
      />

      {showCreateModal && (
        <Modal title="Создать аукцион" onClose={() => setShowCreateModal(false)}>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Дата начала</label>
              <input
                type="date"
                className="input"
                value={createDate}
                onChange={e => setCreateDate(e.target.value)}
              />
              <p className="text-xs text-gray-400 mt-1">Формат будет преобразован в дд.мм.гггг</p>
            </div>
            {createError && <div className="bg-red-50 text-red-700 text-sm rounded px-3 py-2">{createError}</div>}
            <div className="flex gap-3 justify-end">
              <button onClick={() => setShowCreateModal(false)} className="btn-secondary">Отмена</button>
              <button
                onClick={() => {
                  if (!createDate) return
                  const [y, m, d] = createDate.split('-')
                  createMutation.mutate(`${d}.${m}.${y}`)
                }}
                disabled={createMutation.isPending}
                className="btn-primary"
              >
                {createMutation.isPending ? 'Создание...' : 'Создать'}
              </button>
            </div>
          </div>
        </Modal>
      )}
    </div>
  )
}
