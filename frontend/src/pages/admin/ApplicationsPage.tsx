import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { api } from '../../lib/api'
import type { AuctionParticipateApplicationListItem } from '../../types'
import Pagination from '../../components/Pagination'

export default function ApplicationsPage() {
  const qc = useQueryClient()
  const [page, setPage] = useState(1)

  const { data, isLoading } = useQuery({
    queryKey: ['applications', page],
    queryFn: async () => {
      const { data } = await api.get(`/api/v1/auctions/participants/participate-application/?page=${page}&count=30`)
      return data
    },
  })

  const actionMutation = useMutation({
    mutationFn: ({ auctionId, participantId, action }: { auctionId: string; participantId: string; action: 'accepted' | 'declined' }) =>
      api.post(`/api/v1/auctions/${auctionId}/participants/participate-application/${action}/`, JSON.stringify(participantId), {
        headers: { 'Content-Type': 'application/json' },
      }),
    onSuccess: () => qc.invalidateQueries({ queryKey: ['applications'] }),
  })

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-6">Заявки на участие</h1>

      {isLoading ? (
        <div className="space-y-3">
          {Array.from({ length: 5 }).map((_, i) => (
            <div key={i} className="card p-4 animate-pulse h-20" />
          ))}
        </div>
      ) : data?.items?.length === 0 ? (
        <div className="text-center py-16 text-gray-400">
          <div className="text-5xl mb-3">📋</div>
          <p>Заявок нет</p>
        </div>
      ) : (
        <div className="card overflow-hidden">
          <table className="w-full text-sm">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                <th className="px-4 py-3 text-left font-medium text-gray-700">Участник</th>
                <th className="px-4 py-3 text-left font-medium text-gray-700">Аукцион</th>
                <th className="px-4 py-3 text-left font-medium text-gray-700">Дата заявки</th>
                <th className="px-4 py-3 text-left font-medium text-gray-700">Лоты</th>
                <th className="px-4 py-3 text-right font-medium text-gray-700">Действия</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-50">
              {data?.items?.map((app: AuctionParticipateApplicationListItem, i: number) => (
                <tr key={i} className="hover:bg-gray-50">
                  <td className="px-4 py-3">
                    <p className="font-medium text-gray-900">{app.participant?.fullName}</p>
                    <p className="text-xs text-gray-400">{app.participant?.email}</p>
                  </td>
                  <td className="px-4 py-3">
                    <p className="font-medium">Аукцион №{app.auction?.number}</p>
                    <p className="text-xs text-gray-400">{app.auction?.startDate}</p>
                  </td>
                  <td className="px-4 py-3 text-gray-600">{app.createdAt}</td>
                  <td className="px-4 py-3 text-gray-600">{app.lots?.length ?? 0}</td>
                  <td className="px-4 py-3 text-right">
                    <div className="flex justify-end gap-2">
                      {app.actions?.includes('ACCEPT') && (
                        <button
                          onClick={() => actionMutation.mutate({ auctionId: app.auction.id, participantId: app.participant.guid, action: 'accepted' })}
                          disabled={actionMutation.isPending}
                          className="btn-success text-xs px-2 py-1"
                        >✓ Принять</button>
                      )}
                      {app.actions?.includes('DECLINE') && (
                        <button
                          onClick={() => actionMutation.mutate({ auctionId: app.auction.id, participantId: app.participant.guid, action: 'declined' })}
                          disabled={actionMutation.isPending}
                          className="btn-danger text-xs px-2 py-1"
                        >✗ Отклонить</button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <Pagination page={page} pageCount={data?.page?.pageCount ?? 1} onChange={setPage} />
    </div>
  )
}
