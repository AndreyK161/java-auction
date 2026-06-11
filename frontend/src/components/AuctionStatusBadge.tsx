import type { AuctionStatus } from '../types'

export default function AuctionStatusBadge({ status }: { status: AuctionStatus }) {
  const cls = {
    PUBLISHED: 'badge-yellow',
    ACTIVE: 'badge-green',
    ARCHIVE: 'badge-gray',
  }[status.code] ?? 'badge-gray'
  return <span className={cls}>{status.name}</span>
}
