export type UserRole = 'ADMIN' | 'PARTICIPANT'

export interface ParticipantDetails {
  guid: string
  email: string
  fullName: string
  balance?: number
}

export interface UserProfile {
  guid: string
  email: string
  fullName: string
  role: UserRole
  participant?: ParticipantDetails
}

export interface ListPageData {
  number: number
  perPage: number
  pageCount: number
}

export interface AuctionStatus {
  code: 'PUBLISHED' | 'ACTIVE' | 'ARCHIVE'
  name: string
}

export interface AuctionListItem {
  guid: string
  number: number
  startDate: string
  status: AuctionStatus
  lotsCount: number
  participantCount: number
  budget: number
  participation: boolean
  actions: string[]
}

export interface LotPhotoDto {
  guid: string
  url: string
}

export type AuctionLotStatus =
  | 'NEW'
  | 'AUCTION_REQUEST'
  | 'WAITING_FOR_TRADING'
  | 'ON_TRADE'
  | 'WITHDRAW_FROM_AUCTION'
  | 'SOLD'

export interface LotBid {
  amount: number
  participantNumber: number
  participant?: ParticipantDetails
  dateTime?: string
}

export interface LotDetails {
  guid: string
  title: string
  description: string
  startPrice: number
  priceStep: number
  photos: LotPhotoDto[]
  tradeStatus?: AuctionLotStatus
  owner?: ParticipantDetails
  deleted?: boolean
}

export interface LotListItem {
  guid: string
  title: string
  startPrice: number
  createdAt?: string
  photos: LotPhotoDto[]
  actions?: string[]
}

export interface TradingLotListItem {
  guid: string
  title: string
  startPrice: number
  photos: LotPhotoDto[]
  lastBid?: LotBid
  bidCount: number
}

export interface ParticipantListItem {
  guid: string
  id: string
  email: string
  fullName: string
  forSaleLots: LotListItem[]
  purchaseLots: LotListItem[]
  actions?: string[]
}

export interface AuctionDetails {
  id: string
  number: number
  startDate: string
  status: AuctionStatus
  tradingLots: TradingLotListItem[]
  participants: ParticipantListItem[]
}

export interface AuctionLotDetails {
  number: number
  status: AuctionLotStatus
  lot: LotDetails
  bids: LotBid[]
  lastBid?: LotBid
  winner?: ParticipantDetails
  totalAmount?: number
  actions?: string[]
}

export interface ParticipantAccount {
  participant: string
  amount: number
  transactions: { at: string; amount: number; comment: string }[]
}

export interface AccountTransactionListItem {
  datetime: string
  amount: number
  type: 'DEPOSIT' | 'WITHDRAW'
  comment: string
}

export interface AuctionParticipateApplicationListItem {
  createdAt: string
  participant: ParticipantDetails
  auction: {
    id: string
    startDate: string
    status: AuctionStatus
    number: number
  }
  lots: { guid: string }[]
  actions: ('ACCEPT' | 'DECLINE')[]
}

export interface BoughtLotListItem {
  guid: string
  title: string
  startPrice: number
  photos: LotPhotoDto[]
  purchaseInfo: {
    date: string
    participant: ParticipantDetails
    cost: number
  }
}
