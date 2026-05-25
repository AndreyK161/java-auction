package ws.academy.auction.core.enrichers;

import ws.academy.auction.api.dto.rq.bids.BidRq;
import ws.academy.auction.api.dto.rq.bids.CreateBidRq;
import ws.academy.auction.api.dto.rs.bids.BidRs;
import ws.academy.auction.core.dto.BidMessage;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Bid;
import ws.academy.auction.core.entity.ParticipantAuction;

import java.util.UUID;

public interface BidEnricher {
    BidRs buildBidRs(Bid bid, ParticipantAuction participantAuction);

    BidRq buildBidRq(Bid bid);

    Bid buildBid(BidMessage bidMessage);

    BidMessage buildBidMessage(AuctionLot auctionLot, CreateBidRq request, UUID bidGuid);
}
