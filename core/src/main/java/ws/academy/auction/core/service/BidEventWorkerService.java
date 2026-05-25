package ws.academy.auction.core.service;

import ws.academy.auction.core.dto.BidMessage;
import ws.academy.auction.core.entity.Bid;

public interface BidEventWorkerService {
    Bid workBidEvent(BidMessage bidMessage);
}
