package ws.academy.auction.core.enrichers;

import ws.academy.auction.api.dto.rs.auctions.AuctionLotListItem;
import ws.academy.auction.core.entity.Auction;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Lot;

public interface AuctionLotEnricher {
    AuctionLotListItem getAuctionLotListItem(AuctionLot auctionLot);

    AuctionLot buildAuctionLot(Auction auction, Lot lot);
}
