package ws.academy.auction.core.helpers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Bid;
import ws.academy.auction.core.exception.NotFoundException;
import ws.academy.auction.core.helpers.BidHelper;
import ws.academy.auction.core.repository.BidRepository;

@Component
@RequiredArgsConstructor
public class BidHelperImpl implements BidHelper {
    private final BidRepository bidRepository;

    @Override
    public Bid getBidForMaxBuyerNumber(AuctionLot auctionLot) {
        Integer maxBidNumber = bidRepository.findMaxNumber(auctionLot);
        return bidRepository.findByBidNumberAndAuctionLot(maxBidNumber, auctionLot)
                .orElseThrow(() -> new NotFoundException("Ставок не найдено"));
    }
}
