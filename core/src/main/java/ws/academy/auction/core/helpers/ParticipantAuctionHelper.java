package ws.academy.auction.core.helpers;

import ws.academy.auction.core.entity.Auction;
import ws.academy.auction.core.entity.Participant;
import ws.academy.auction.core.entity.ParticipantAuction;

public interface ParticipantAuctionHelper {
    ParticipantAuction getParticipantAuctionOrThrow(Auction auction, Participant participant);
}
