package ws.academy.auction.core.helpers;

import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Bid;

public interface BidHelper {
    Bid getBidForMaxBuyerNumber(AuctionLot auctionLot);
}
