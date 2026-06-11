interface Props {
  page: number
  pageCount: number
  onChange: (p: number) => void
}

export default function Pagination({ page, pageCount, onChange }: Props) {
  if (pageCount <= 1) return null
  const pages = Array.from({ length: Math.min(pageCount, 7) }, (_, i) => {
    if (pageCount <= 7) return i + 1
    if (page <= 4) return i + 1
    if (page >= pageCount - 3) return pageCount - 6 + i
    return page - 3 + i
  })

  return (
    <div className="flex items-center justify-center gap-1 mt-6">
      <button
        disabled={page === 1}
        onClick={() => onChange(page - 1)}
        className="btn-secondary px-2 py-1 text-sm disabled:opacity-40"
      >‹</button>
      {pages.map(p => (
        <button
          key={p}
          onClick={() => onChange(p)}
          className={`px-3 py-1 rounded text-sm font-medium ${p === page ? 'bg-blue-600 text-white' : 'text-gray-600 hover:bg-gray-100'}`}
        >{p}</button>
      ))}
      <button
        disabled={page === pageCount}
        onClick={() => onChange(page + 1)}
        className="btn-secondary px-2 py-1 text-sm disabled:opacity-40"
      >›</button>
    </div>
  )
}
