package ws.academy.auction.core.enrichers;

import ws.academy.auction.api.dto.rs.auctions.AuctionRs;
import ws.academy.auction.api.dto.rs.lots.LotDetails;
import ws.academy.auction.api.dto.rs.lots.LotSummaryRs;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Lot;

public interface LotEnricher {
    LotDetails bindLotWithDetails(Lot lot, ParticipantDetails participant, AuctionRs auctionRs);

    LotSummaryRs toLotSummary(AuctionLot auctionLot);
}
