package ws.academy.auction.core.enrichers;

import ws.academy.auction.api.dto.rs.auctions.AuctionRs;
import ws.academy.auction.api.dto.rs.lots.AuctionLotRs;
import ws.academy.auction.core.entity.*;

import java.util.List;

public interface AuctionEnricher {
    AuctionRs toAuctionDto(Auction auction);

    List<AuctionLotRs> getTradingLots(Auction auction);

    AuctionLotRs getAuctionLotRs(Lot lot, Bid bid, ParticipantAuction participantAuction, AuctionLot auctionLot);

    void setPhotos(AuctionRs auctionRs);
}
