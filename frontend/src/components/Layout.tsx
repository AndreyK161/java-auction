import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Layout({ children }: { children: React.ReactNode }) {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const handleLogout = () => {
    logout()
    navigate('/')
  }

  const isActive = (path: string) =>
    location.pathname === path || location.pathname.startsWith(path + '/')
      ? 'text-blue-600 font-medium'
      : 'text-gray-600 hover:text-gray-900'

  return (
    <div className="min-h-screen flex flex-col">
      <header className="bg-white border-b border-gray-200 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center gap-8">
              <Link to="/" className="flex items-center gap-2 font-bold text-xl text-blue-600">
                <span>🏷️</span>
                <span>АукционПро</span>
              </Link>
              <nav className="hidden md:flex items-center gap-6">
                <Link to="/" className={`text-sm ${isActive('/')}`}>Аукционы</Link>
                {user && (
                  <>
                    <Link to="/lots" className={`text-sm ${isActive('/lots')}`}>Мои лоты</Link>
                    {user.role === 'ADMIN' && (
                      <>
                        <Link to="/admin/users" className={`text-sm ${isActive('/admin/users')}`}>Пользователи</Link>
                        <Link to="/admin/applications" className={`text-sm ${isActive('/admin/applications')}`}>Заявки</Link>
                      </>
                    )}
                  </>
                )}
              </nav>
            </div>
            <div className="flex items-center gap-3">
              {user ? (
                <>
                  <Link to="/profile" className="flex items-center gap-2 text-sm text-gray-700 hover:text-gray-900">
                    <div className="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center text-blue-700 font-medium text-xs">
                      {user.fullName?.charAt(0) || user.email.charAt(0).toUpperCase()}
                    </div>
                    <span className="hidden sm:block">{user.fullName || user.email}</span>
                  </Link>
                  <button onClick={handleLogout} className="btn-secondary text-sm px-3 py-1.5">
                    Выйти
                  </button>
                </>
              ) : (
                <>
                  <Link to="/login" className="btn-secondary text-sm">Войти</Link>
                  <Link to="/register" className="btn-primary text-sm">Регистрация</Link>
                </>
              )}
            </div>
          </div>
        </div>
      </header>
      <main className="flex-1">
        {children}
      </main>
      <footer className="bg-white border-t border-gray-200 py-6 mt-12">
        <div className="max-w-7xl mx-auto px-4 text-center text-sm text-gray-500">
          © 2026 АукционПро — Электронная торговая площадка
        </div>
      </footer>
    </div>
  )
}
