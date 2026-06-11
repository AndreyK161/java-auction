import { useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../lib/api'

export default function PasswordRecoveryPage() {
  const [email, setEmail] = useState('')
  const [success, setSuccess] = useState(false)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await api.post('/api/v1/auth/password-recovery/', { email })
      setSuccess(true)
    } catch {
      setError('Не удалось отправить письмо. Проверьте email.')
    } finally {
      setLoading(false)
    }
  }

  if (success) {
    return (
      <div className="min-h-[80vh] flex items-center justify-center px-4">
        <div className="card p-8 w-full max-w-md text-center">
          <div className="text-5xl mb-4">📧</div>
          <h2 className="text-xl font-bold mb-2">Письмо отправлено</h2>
          <p className="text-gray-500">Проверьте почту <strong>{email}</strong></p>
          <Link to="/login" className="btn-secondary mt-6 inline-block">Назад</Link>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-4">
      <div className="card p-8 w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-2xl font-bold text-gray-900">Восстановление пароля</h1>
          <p className="text-gray-500 text-sm mt-1">Мы отправим ссылку на ваш email</p>
        </div>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input type="email" className="input" value={email} onChange={e => setEmail(e.target.value)} required />
          </div>
          {error && <div className="bg-red-50 border border-red-200 text-red-700 text-sm rounded-lg px-4 py-3">{error}</div>}
          <button type="submit" disabled={loading} className="btn-primary w-full py-2.5">
            {loading ? '...' : 'Отправить ссылку'}
          </button>
        </form>
        <Link to="/login" className="block text-center mt-4 text-sm text-blue-600 hover:underline">Назад</Link>
      </div>
    </div>
  )
}
