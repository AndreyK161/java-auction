import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { api } from '../../lib/api'
import type { UserProfile } from '../../types'
import Pagination from '../../components/Pagination'
import Modal from '../../components/Modal'

export default function UsersPage() {
  const qc = useQueryClient()
  const [page, setPage] = useState(1)
  const [search, setSearch] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [editUser, setEditUser] = useState<UserProfile | null>(null)
  const [form, setForm] = useState({ fullName: '', email: '', role: 'PARTICIPANT' })
  const [formError, setFormError] = useState('')

  const { data, isLoading } = useQuery({
    queryKey: ['users', page, search],
    queryFn: async () => {
      const params = new URLSearchParams({ page: String(page), count: '20' })
      if (search) params.append('name', search)
      const { data } = await api.get(`/api/v1/users/?${params}`)
      return data
    },
  })

  const createMutation = useMutation({
    mutationFn: () => api.post('/api/v1/users/', form),
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['users'] }); setShowModal(false) },
    onError: (err: any) => setFormError(err.response?.data?.message || 'Ошибка'),
  })

  const updateMutation = useMutation({
    mutationFn: () => api.put(`/api/v1/users/${editUser?.guid}/`, { fullName: form.fullName, role: form.role }),
    onSuccess: () => { qc.invalidateQueries({ queryKey: ['users'] }); setEditUser(null) },
    onError: (err: any) => setFormError(err.response?.data?.message || 'Ошибка'),
  })

  const deleteMutation = useMutation({
    mutationFn: (guid: string) => api.delete(`/api/v1/users/${guid}/`),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['users'] }),
  })

  const openEdit = (u: UserProfile) => {
    setEditUser(u)
    setForm({ fullName: u.fullName || '', email: u.email, role: u.role })
    setFormError('')
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-3xl font-bold text-gray-900">Пользователи</h1>
        <button onClick={() => { setShowModal(true); setForm({ fullName: '', email: '', role: 'PARTICIPANT' }); setFormError('') }} className="btn-primary">
          + Добавить пользователя
        </button>
      </div>

      <div className="mb-4">
        <input
          type="text"
          className="input max-w-sm"
          placeholder="🔍 Поиск по имени..."
          value={search}
          onChange={e => { setSearch(e.target.value); setPage(1) }}
        />
      </div>

      <div className="card overflow-hidden">
        <table className="w-full text-sm">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              <th className="px-4 py-3 text-left font-medium text-gray-700">ФИО</th>
              <th className="px-4 py-3 text-left font-medium text-gray-700">Email</th>
              <th className="px-4 py-3 text-left font-medium text-gray-700">Роль</th>
              <th className="px-4 py-3 text-right font-medium text-gray-700">Действия</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-50">
            {isLoading ? (
              Array.from({ length: 5 }).map((_, i) => (
                <tr key={i}>
                  <td colSpan={4} className="px-4 py-3">
                    <div className="h-4 bg-gray-100 rounded animate-pulse" />
                  </td>
                </tr>
              ))
            ) : data?.items?.map((u: UserProfile) => (
              <tr key={u.guid} className="hover:bg-gray-50">
                <td className="px-4 py-3">
                  <div className="flex items-center gap-3">
                    <div className="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center text-blue-700 font-medium text-xs flex-shrink-0">
                      {u.fullName?.charAt(0) || u.email.charAt(0).toUpperCase()}
                    </div>
                    <span className="font-medium text-gray-900">{u.fullName || '—'}</span>
                  </div>
                </td>
                <td className="px-4 py-3 text-gray-600">{u.email}</td>
                <td className="px-4 py-3">
                  <span className={u.role === 'ADMIN' ? 'badge-blue' : 'badge-gray'}>
                    {u.role === 'ADMIN' ? 'Админ' : 'Участник'}
                  </span>
                </td>
                <td className="px-4 py-3 text-right">
                  <div className="flex justify-end gap-2">
                    <button onClick={() => openEdit(u)} className="btn-secondary text-xs px-2 py-1">Изменить</button>
                    <button
                      onClick={() => { if (confirm('Удалить пользователя?')) deleteMutation.mutate(u.guid) }}
                      className="btn-danger text-xs px-2 py-1"
                    >Удалить</button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {data?.items?.length === 0 && (
          <div className="text-center py-10 text-gray-400">Пользователи не найдены</div>
        )}
      </div>

      <Pagination page={page} pageCount={data?.page?.pageCount ?? 1} onChange={setPage} />

      {(showModal || editUser) && (
        <Modal title={editUser ? 'Редактировать пользователя' : 'Новый пользователь'} onClose={() => { setShowModal(false); setEditUser(null) }}>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">ФИО</label>
              <input type="text" className="input" value={form.fullName} onChange={e => setForm(f => ({ ...f, fullName: e.target.value }))} />
            </div>
            {!editUser && (
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <input type="email" className="input" value={form.email} onChange={e => setForm(f => ({ ...f, email: e.target.value }))} />
              </div>
            )}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Роль</label>
              <select className="input" value={form.role} onChange={e => setForm(f => ({ ...f, role: e.target.value }))}>
                <option value="PARTICIPANT">Участник</option>
                <option value="ADMIN">Администратор</option>
              </select>
            </div>
            {formError && <div className="bg-red-50 text-red-700 text-sm rounded px-3 py-2">{formError}</div>}
            <div className="flex gap-3 justify-end">
              <button onClick={() => { setShowModal(false); setEditUser(null) }} className="btn-secondary">Отмена</button>
              <button
                onClick={() => editUser ? updateMutation.mutate() : createMutation.mutate()}
                disabled={createMutation.isPending || updateMutation.isPending}
                className="btn-primary"
              >Сохранить</button>
            </div>
          </div>
        </Modal>
      )}
    </div>
  )
}
