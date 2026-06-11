import { useState } from 'react'
import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Layout({ children }: { children: React.ReactNode }) {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [mobileOpen, setMobileOpen] = useState(false)

  const handleLogout = () => {
    logout()
    navigate('/')
    setMobileOpen(false)
  }

  const isActive = (path: string) =>
    location.pathname === path || location.pathname.startsWith(path + '/')
      ? 'text-blue-600 font-medium'
      : 'text-gray-600 hover:text-gray-900'

  const navLinks = (
    <>
      <Link to="/" onClick={() => setMobileOpen(false)} className={`text-sm ${isActive('/')}`}>Аукционы</Link>
      {user && (
        <>
          <Link to="/lots" onClick={() => setMobileOpen(false)} className={`text-sm ${isActive('/lots')}`}>Мои лоты</Link>
          {user.role === 'ADMIN' && (
            <>
              <Link to="/admin/users" onClick={() => setMobileOpen(false)} className={`text-sm ${isActive('/admin/users')}`}>Пользователи</Link>
              <Link to="/admin/applications" onClick={() => setMobileOpen(false)} className={`text-sm ${isActive('/admin/applications')}`}>Заявки</Link>
            </>
          )}
        </>
      )}
    </>
  )

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
                {navLinks}
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
                  <button onClick={handleLogout} className="btn-secondary text-sm px-3 py-1.5 hidden md:block">
                    Выйти
                  </button>
                </>
              ) : (
                <>
                  <Link to="/login" className="btn-secondary text-sm hidden sm:inline-flex">Войти</Link>
                  <Link to="/register" className="btn-primary text-sm">Регистрация</Link>
                </>
              )}

              <button
                onClick={() => setMobileOpen(o => !o)}
                className="md:hidden p-2 rounded-lg text-gray-500 hover:bg-gray-100"
                aria-label="Меню"
              >
                {mobileOpen ? (
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                  </svg>
                ) : (
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                  </svg>
                )}
              </button>
            </div>
          </div>
        </div>

        {mobileOpen && (
          <div className="md:hidden border-t border-gray-100 bg-white px-4 py-3 flex flex-col gap-3">
            {navLinks}
            {user ? (
              <button onClick={handleLogout} className="btn-secondary text-sm w-full mt-1">Выйти</button>
            ) : (
              <Link to="/login" onClick={() => setMobileOpen(false)} className="btn-secondary text-sm text-center">Войти</Link>
            )}
          </div>
        )}
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
