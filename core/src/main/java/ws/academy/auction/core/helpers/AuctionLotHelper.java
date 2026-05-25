package ws.academy.auction.core.helpers;

import ws.academy.auction.core.entity.Auction;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Lot;

public interface AuctionLotHelper {
    AuctionLot getAuctionLotOrThrow(Auction auction, Lot lot);

    AuctionLot getAuctionLotOrThrowWithLot(Lot lot);

    AuctionLot getArchiveAuctionLotFor(Lot lot);
}
