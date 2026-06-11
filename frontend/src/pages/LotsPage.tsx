import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { api } from '../lib/api'
import { useAuth } from '../context/AuthContext'
import type { LotListItem } from '../types'
import LotStatusBadge from '../components/LotStatusBadge'
import Pagination from '../components/Pagination'
import Modal from '../components/Modal'

function formatMoney(n: number) {
  return new Intl.NumberFormat('ru-RU', { style: 'currency', currency: 'RUB', maximumFractionDigits: 0 }).format(n)
}

interface LotFormData {
  title: string
  description: string
  startPrice: string
  priceStep: string
}

export default function LotsPage() {
  const { user } = useAuth()
  const qc = useQueryClient()
  const [page, setPage] = useState(1)
  const [search, setSearch] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [editLot, setEditLot] = useState<any>(null)
  const [form, setForm] = useState<LotFormData>({ title: '', description: '', startPrice: '', priceStep: '' })
  const [formError, setFormError] = useState('')
  const [photoFile, setPhotoFile] = useState<File | null>(null)

  const { data, isLoading } = useQuery({
    queryKey: ['lots', page, search],
    queryFn: async () => {
      const params = new URLSearchParams({ page: String(page), count: '30' })
      if (search) params.append('name', search)
      if (user?.participant?.guid) params.append('owner', user.participant.guid)
      const { data } = await api.get(`/api/v1/lots/?${params}`)
      return data
    },
  })

  const uploadPhoto = async (file: File) => {
    const fd = new FormData()
    fd.append('file', file)
    const { data } = await api.post('/api/v1/file/', fd)
    return data.guid
  }

  const createMutation = useMutation({
    mutationFn: async (values: LotFormData & { file: File | null }) => {
      if (!user?.participant?.guid) throw new Error('Только участники могут создавать лоты')
      let photo: { guid: string }[] = []
      if (values.file) {
        const guid = await uploadPhoto(values.file)
        photo = [{ guid }]
      }
      return api.post('/api/v1/lots/', {
        title: values.title,
        description: values.description,
        startPrice: Number(values.startPrice),
        priceStep: Number(values.priceStep),
        photo,
        owner: { guid: user.participant.guid },
      })
    },
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['lots'] })
      setShowModal(false)
      setForm({ title: '', description: '', startPrice: '', priceStep: '' })
      setPhotoFile(null)
    },
    onError: (err: any) => setFormError(err.response?.data?.message || 'Ошибка'),
  })

  const updateMutation = useMutation({
    mutationFn: async ({ guid, values, file }: { guid: string; values: LotFormData; file: File | null }) => {
      let photo: { guid: string }[] | undefined
      if (file) {
        const g = await uploadPhoto(file)
        photo = [{ guid: g }]
      }
      return api.put(`/api/v1/lots/${guid}/`, {
        title: values.title,
        description: values.description,
        startPrice: Number(values.startPrice),
        ...(photo && { photo }),
      })
    },
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['lots'] })
      setEditLot(null)
    },
    onError: (err: any) => setFormError(err.response?.data?.message || 'Ошибка'),
  })

  const deleteMutation = useMutation({
    mutationFn: (guid: string) => api.delete(`/api/v1/lots/${guid}/`),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['lots'] }),
  })

  const openEdit = (lot: any) => {
    setEditLot(lot)
    setForm({ title: lot.title, description: lot.description || '', startPrice: String(lot.startPrice), priceStep: String(lot.priceStep || '') })
    setFormError('')
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex items-start justify-between gap-4 mb-6">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Мои лоты</h1>
          <p className="text-gray-500 mt-1">Управление лотами</p>
        </div>
        <button
          onClick={() => { setShowModal(true); setForm({ title: '', description: '', startPrice: '', priceStep: '' }); setFormError('') }}
          className="btn-primary"
        >+ Добавить лот</button>
      </div>

      <div className="mb-4">
        <input
          type="text"
          className="input max-w-sm"
          placeholder="🔍 Поиск по названию..."
          value={search}
          onChange={e => { setSearch(e.target.value); setPage(1) }}
        />
      </div>

      {isLoading ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {Array.from({ length: 6 }).map((_, i) => (
            <div key={i} className="card p-4 animate-pulse">
              <div className="h-40 bg-gray-200 rounded-lg mb-3" />
              <div className="h-4 bg-gray-200 rounded w-2/3 mb-2" />
              <div className="h-4 bg-gray-100 rounded w-1/3" />
            </div>
          ))}
        </div>
      ) : data?.items?.length === 0 ? (
        <div className="text-center py-16 text-gray-400">
          <div className="text-5xl mb-4">📦</div>
          <p className="text-lg">Лоты не найдены</p>
          <button onClick={() => setShowModal(true)} className="btn-primary mt-4">Добавить первый лот</button>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {data?.items?.map((lot: LotListItem & { tradeStatus?: any; priceStep?: number; description?: string }) => (
            <div key={lot.guid} className="card overflow-hidden">
              {lot.photos?.[0]?.url ? (
                <img
                  src={lot.photos[0].url}
                  alt={lot.title}
                  className="w-full h-40 object-cover"
                />
              ) : (
                <div className="w-full h-40 bg-gradient-to-br from-blue-50 to-blue-100 flex items-center justify-center text-4xl">📦</div>
              )}
              <div className="p-4">
                <div className="flex items-start justify-between mb-2">
                  <h3 className="font-semibold text-gray-900 truncate">{lot.title}</h3>
                  {lot.tradeStatus && <LotStatusBadge status={lot.tradeStatus} />}
                </div>
                <p className="text-lg font-bold text-gray-900 mb-1">{formatMoney(lot.startPrice)}</p>
                {lot.createdAt && <p className="text-xs text-gray-400 mb-3">{lot.createdAt}</p>}
                <div className="flex gap-2">
                  <button onClick={() => openEdit(lot)} className="btn-secondary text-xs flex-1">Редактировать</button>
                  {lot.actions?.includes('DELETE') && (
                    <button
                      onClick={() => { if (confirm('Удалить лот?')) deleteMutation.mutate(lot.guid) }}
                      className="btn-danger text-xs px-2"
                    >🗑</button>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      <Pagination page={page} pageCount={data?.page?.pageCount ?? 1} onChange={setPage} />

      {(showModal || editLot) && (
        <Modal
          title={editLot ? 'Редактировать лот' : 'Новый лот'}
          onClose={() => { setShowModal(false); setEditLot(null) }}
        >
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Название</label>
              <input type="text" className="input" value={form.title} onChange={e => setForm(f => ({ ...f, title: e.target.value }))} required />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Описание</label>
              <textarea rows={3} className="input resize-none" value={form.description} onChange={e => setForm(f => ({ ...f, description: e.target.value }))} />
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Начальная цена (₽)</label>
                <input type="number" className="input" value={form.startPrice} onChange={e => setForm(f => ({ ...f, startPrice: e.target.value }))} required />
              </div>
              {!editLot && (
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Шаг цены (₽)</label>
                  <input type="number" className="input" value={form.priceStep} onChange={e => setForm(f => ({ ...f, priceStep: e.target.value }))} required />
                </div>
              )}
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Фото</label>
              <input
                type="file"
                accept="image/*"
                className="text-sm text-gray-500 cursor-pointer"
                onChange={e => setPhotoFile(e.target.files?.[0] || null)}
              />
            </div>
            {formError && <div className="bg-red-50 text-red-700 text-sm rounded px-3 py-2">{formError}</div>}
            <div className="flex gap-3 justify-end">
              <button onClick={() => { setShowModal(false); setEditLot(null) }} className="btn-secondary">Отмена</button>
              <button
                onClick={() => {
                  if (editLot) {
                    updateMutation.mutate({ guid: editLot.guid, values: form, file: photoFile })
                  } else {
                    createMutation.mutate({ ...form, file: photoFile })
                  }
                }}
                disabled={createMutation.isPending || updateMutation.isPending}
                className="btn-primary"
              >
                {createMutation.isPending || updateMutation.isPending ? 'Сохранение...' : 'Сохранить'}
              </button>
            </div>
          </div>
        </Modal>
      )}
    </div>
  )
}
