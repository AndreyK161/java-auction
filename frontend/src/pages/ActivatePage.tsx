import { useState } from 'react'
import { useSearchParams, Link } from 'react-router-dom'
import { api } from '../lib/api'

export default function ActivatePage() {
  const [params] = useSearchParams()
  const [password, setPassword] = useState('')
  const [password2, setPassword2] = useState('')
  const [success, setSuccess] = useState(false)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const email = params.get('email') || ''
  const hash = params.get('hash') || ''

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (password !== password2) { setError('Пароли не совпадают'); return }
    setError('')
    setLoading(true)
    try {
      await api.post('/api/v1/auth/activate/', { email, hash, password })
      setSuccess(true)
    } catch (err: any) {
      setError(err.response?.data?.message || 'Ошибка активации')
    } finally {
      setLoading(false)
    }
  }

  if (!email || !hash) {
    return (
      <div className="min-h-[80vh] flex items-center justify-center">
        <div className="card p-8 text-center">
          <p className="text-red-600">Неверная ссылка активации</p>
        </div>
      </div>
    )
  }

  if (success) {
    return (
      <div className="min-h-[80vh] flex items-center justify-center px-4">
        <div className="card p-8 w-full max-w-md text-center">
          <div className="text-5xl mb-4">✅</div>
          <h2 className="text-xl font-bold text-gray-900 mb-2">Аккаунт активирован!</h2>
          <p className="text-gray-500">Теперь вы можете войти в систему</p>
          <Link to="/login" className="btn-primary mt-6 inline-block">Войти</Link>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-4">
      <div className="card p-8 w-full max-w-md">
        <div className="text-center mb-8">
          <div className="text-4xl mb-3">🔑</div>
          <h1 className="text-2xl font-bold text-gray-900">Активация аккаунта</h1>
          <p className="text-gray-500 text-sm mt-1">{email}</p>
        </div>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Придумайте пароль</label>
            <input type="password" className="input" value={password} onChange={e => setPassword(e.target.value)} required minLength={8} />
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Повторите пароль</label>
            <input type="password" className="input" value={password2} onChange={e => setPassword2(e.target.value)} required />
          </div>
          {error && <div className="bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg px-4 py-3">{error}</div>}
          <button type="submit" disabled={loading} className="btn-primary w-full py-2.5">
            {loading ? '...' : 'Активировать'}
          </button>
        </form>
      </div>
    </div>
  )
}
