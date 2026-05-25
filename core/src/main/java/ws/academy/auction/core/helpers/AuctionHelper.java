package ws.academy.auction.core.helpers;

import ws.academy.auction.core.entity.Auction;

import java.util.UUID;

public interface AuctionHelper {
    Auction getAuctionOrThrow(UUID guid);
}
