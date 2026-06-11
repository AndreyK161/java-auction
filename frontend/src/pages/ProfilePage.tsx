import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { api } from '../lib/api'
import { useAuth } from '../context/AuthContext'
import type { ParticipantAccount, AccountTransactionListItem, BoughtLotListItem } from '../types'
import Pagination from '../components/Pagination'
import Modal from '../components/Modal'

function formatMoney(n: number) {
  return new Intl.NumberFormat('ru-RU', { style: 'currency', currency: 'RUB', maximumFractionDigits: 0 }).format(n)
}

type Tab = 'profile' | 'account' | 'transactions' | 'purchases'

export default function ProfilePage() {
  const { user, refetchUser } = useAuth()
  const qc = useQueryClient()
  const [tab, setTab] = useState<Tab>('profile')
  const [txPage, setTxPage] = useState(1)
  const [purchasesPage, setPurchasesPage] = useState(1)
  const [fullName, setFullName] = useState(user?.fullName || '')
  const [editSuccess, setEditSuccess] = useState(false)
  const [depositModal, setDepositModal] = useState(false)
  const [withdrawModal, setWithdrawModal] = useState(false)
  const [depositAmount, setDepositAmount] = useState('')
  const [withdrawAmount, setWithdrawAmount] = useState('')
  const [cardNumber, setCardNumber] = useState('')
  const [cardExpire, setCardExpire] = useState('')
  const [cardCvv, setCardCvv] = useState('')

  const participantGuid = user?.participant?.guid

  const { data: account } = useQuery<ParticipantAccount>({
    queryKey: ['account', participantGuid],
    queryFn: async () => {
      const { data } = await api.get(`/api/v1/participants/${participantGuid}/account/`)
      return data
    },
    enabled: !!participantGuid && (tab === 'account' || tab === 'transactions'),
  })

  const { data: txData } = useQuery({
    queryKey: ['transactions', participantGuid, txPage],
    queryFn: async () => {
      const { data } = await api.get(`/api/v1/participants/${participantGuid}/account/transactions/?page=${txPage}&count=30`)
      return data
    },
    enabled: !!participantGuid && tab === 'transactions',
  })

  const { data: purchasesData } = useQuery({
    queryKey: ['purchases', participantGuid, purchasesPage],
    queryFn: async () => {
      const { data } = await api.get(`/api/v1/participant/${participantGuid}/purchases?page=${purchasesPage}&count=30`)
      return data
    },
    enabled: !!participantGuid && tab === 'purchases',
  })

  const updateProfileMutation = useMutation({
    mutationFn: () => api.put('/api/v1/auth/current-user/', { fullName }),
    onSuccess: () => { setEditSuccess(true); refetchUser() },
  })

  const depositMutation = useMutation({
    mutationFn: () => api.put(`/api/v1/participants/${participantGuid}/account/`, {
      amount: Number(depositAmount),
      card: { number: cardNumber, expire: cardExpire, cvv: cardCvv },
    }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['account'] })
      setDepositModal(false)
      setDepositAmount('')
    },
  })

  const withdrawMutation = useMutation({
    mutationFn: () => api.post(`/api/v1/participants/${participantGuid}/account/withdraw/`, {
      amount: Number(withdrawAmount),
      card: cardNumber,
    }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: ['account'] })
      setWithdrawModal(false)
      setWithdrawAmount('')
    },
  })

  const tabs: { key: Tab; label: string; icon: string }[] = [
    { key: 'profile', label: 'Профиль', icon: '👤' },
    { key: 'account', label: 'Счёт', icon: '💳' },
    { key: 'transactions', label: 'Транзакции', icon: '📊' },
    { key: 'purchases', label: 'Покупки', icon: '🛒' },
  ]

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-6">Личный кабинет</h1>

      <div className="flex gap-1 mb-6 bg-gray-100 p-1 rounded-xl w-fit">
        {tabs.map(t => (
          <button
            key={t.key}
            onClick={() => setTab(t.key)}
            className={`px-4 py-2 rounded-lg text-sm font-medium transition-colors ${
              tab === t.key ? 'bg-white text-gray-900 shadow-sm' : 'text-gray-500 hover:text-gray-700'
            }`}
          >
            {t.icon} {t.label}
          </button>
        ))}
      </div>

      {tab === 'profile' && (
        <div className="card p-6 max-w-lg">
          <h2 className="text-lg font-semibold mb-4">Данные профиля</h2>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">ФИО</label>
              <input type="text" className="input" value={fullName} onChange={e => setFullName(e.target.value)} />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
              <input type="email" className="input bg-gray-50" value={user?.email} disabled />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Роль</label>
              <span className={user?.role === 'ADMIN' ? 'badge-blue' : 'badge-gray'}>
                {user?.role === 'ADMIN' ? 'Администратор' : 'Участник'}
              </span>
            </div>
            {editSuccess && <div className="bg-green-50 text-green-700 text-sm rounded px-3 py-2">Сохранено ✓</div>}
            <button
              onClick={() => { setEditSuccess(false); updateProfileMutation.mutate() }}
              disabled={updateProfileMutation.isPending}
              className="btn-primary"
            >Сохранить</button>
          </div>
        </div>
      )}

      {tab === 'account' && (
        <div className="card p-6 max-w-lg">
          <h2 className="text-lg font-semibold mb-4">Личный счёт</h2>
          <div className="bg-gradient-to-r from-blue-600 to-blue-700 rounded-xl p-6 text-white mb-6">
            <p className="text-blue-200 text-sm mb-1">Баланс</p>
            <p className="text-3xl font-bold">{formatMoney(account?.amount ?? 0)}</p>
          </div>
          <div className="flex gap-3">
            <button onClick={() => setDepositModal(true)} className="btn-primary flex-1">Пополнить</button>
            <button onClick={() => setWithdrawModal(true)} className="btn-secondary flex-1">Вывести</button>
          </div>
        </div>
      )}

      {tab === 'transactions' && (
        <div className="card overflow-hidden">
          <div className="p-4 border-b border-gray-100">
            <h2 className="font-semibold text-gray-900">История транзакций</h2>
          </div>
          {txData?.items?.length === 0 ? (
            <div className="text-center py-10 text-gray-400">Транзакций нет</div>
          ) : (
            <div className="divide-y divide-gray-50">
              {txData?.items?.map((tx: AccountTransactionListItem, i: number) => (
                <div key={i} className="flex items-center justify-between px-4 py-3">
                  <div>
                    <p className="text-sm font-medium text-gray-900">{tx.comment || (tx.type === 'DEPOSIT' ? 'Пополнение' : 'Вывод')}</p>
                    <p className="text-xs text-gray-400">{tx.datetime}</p>
                  </div>
                  <p className={`font-semibold ${tx.type === 'DEPOSIT' || tx.type === 'SALE' ? 'text-green-600' : 'text-red-600'}`}>
                    {tx.type === 'DEPOSIT' || tx.type === 'SALE' ? '+' : '-'}{formatMoney(Math.abs(tx.amount))}
                  </p>
                </div>
              ))}
            </div>
          )}
          <Pagination page={txPage} pageCount={txData?.page?.pageCount ?? 1} onChange={setTxPage} />
        </div>
      )}

      {tab === 'purchases' && (
        <div>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {purchasesData?.items?.map((item: BoughtLotListItem) => (
              <div key={item.guid} className="card overflow-hidden">
                {item.photo?.[0]?.url ? (
                  <img src={item.photo[0].url} alt={item.title} className="w-full h-48 object-cover" />
                ) : (
                  <div className="w-full h-48 bg-gray-100 flex items-center justify-center text-5xl">📦</div>
                )}
                <div className="p-4">
                  <h3 className="font-semibold text-gray-900 mb-2">{item.title}</h3>
                  <p className="text-xs text-gray-400 mb-1">{item.purchaseInfo?.date}</p>
                  <p className="text-lg font-bold text-green-600">{formatMoney(item.purchaseInfo?.cost)}</p>
                </div>
              </div>
            ))}
          </div>
          {purchasesData?.items?.length === 0 && (
            <div className="text-center py-16 text-gray-400">
              <div className="text-5xl mb-3">🛒</div>
              <p>Нет купленных лотов</p>
            </div>
          )}
          <Pagination page={purchasesPage} pageCount={purchasesData?.page?.pageCount ?? 1} onChange={setPurchasesPage} />
        </div>
      )}

      {depositModal && (
        <Modal title="Пополнение счёта" onClose={() => setDepositModal(false)}>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Сумма (₽)</label>
              <input type="number" className="input" value={depositAmount} onChange={e => setDepositAmount(e.target.value)} />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Номер карты</label>
              <input type="text" className="input" placeholder="0000 0000 0000 0000" value={cardNumber} onChange={e => setCardNumber(e.target.value)} maxLength={19} />
            </div>
            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Срок действия</label>
                <input type="text" className="input" placeholder="мм/гг" value={cardExpire} onChange={e => setCardExpire(e.target.value)} />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">CVV</label>
                <input type="password" className="input" placeholder="•••" maxLength={3} value={cardCvv} onChange={e => setCardCvv(e.target.value)} />
              </div>
            </div>
            <div className="flex gap-3 justify-end">
              <button onClick={() => setDepositModal(false)} className="btn-secondary">Отмена</button>
              <button onClick={() => depositMutation.mutate()} disabled={depositMutation.isPending} className="btn-primary">Пополнить</button>
            </div>
          </div>
        </Modal>
      )}

      {withdrawModal && (
        <Modal title="Вывод средств" onClose={() => setWithdrawModal(false)}>
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Сумма (₽)</label>
              <input type="number" className="input" value={withdrawAmount} onChange={e => setWithdrawAmount(e.target.value)} />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Номер карты</label>
              <input type="text" className="input" placeholder="0000 0000 0000 0000" value={cardNumber} onChange={e => setCardNumber(e.target.value)} />
            </div>
            <div className="flex gap-3 justify-end">
              <button onClick={() => setWithdrawModal(false)} className="btn-secondary">Отмена</button>
              <button onClick={() => withdrawMutation.mutate()} disabled={withdrawMutation.isPending} className="btn-primary">Вывести</button>
            </div>
          </div>
        </Modal>
      )}
    </div>
  )
}
