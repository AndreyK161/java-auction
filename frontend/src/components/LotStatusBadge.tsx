import type { AuctionLotStatus } from '../types'

const labels: Record<AuctionLotStatus, string> = {
  NEW: 'Новый',
  AUCTION_REQUEST: 'Заявка',
  WAITING_FOR_TRADING: 'Ожидает торгов',
  ON_TRADE: 'На торгах',
  WITHDRAW_FROM_AUCTION: 'Снят',
  SOLD: 'Продан',
}
const cls: Record<AuctionLotStatus, string> = {
  NEW: 'badge-gray',
  AUCTION_REQUEST: 'badge-yellow',
  WAITING_FOR_TRADING: 'badge-blue',
  ON_TRADE: 'badge-green',
  WITHDRAW_FROM_AUCTION: 'badge-red',
  SOLD: 'badge-gray',
}

export default function LotStatusBadge({ status }: { status: AuctionLotStatus }) {
  return <span className={cls[status]}>{labels[status]}</span>
}
