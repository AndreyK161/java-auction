package ws.academy.auction.core.enrichers;

import ws.academy.auction.api.dto.rq.lots.LotGuidRq;
import ws.academy.auction.api.dto.rs.auctions.AuctionRs;
import ws.academy.auction.api.dto.rs.auctions.AuctionSubmittingRs;
import ws.academy.auction.api.dto.rs.participantAuctions.ParticipantAuctionItem;
import ws.academy.auction.core.entity.Auction;
import ws.academy.auction.core.entity.ParticipantAuction;

import java.util.List;

public interface ParticipantAuctionEnricher {
    AuctionSubmittingRs buildAuctionSubmittingRs(Auction auction);

    ParticipantAuctionItem buildParticipantAuctionItem(ParticipantAuction pa, List<LotGuidRq> lots);

    void setAuctionParticipantList(AuctionRs auctionRs, Auction auction);
}
