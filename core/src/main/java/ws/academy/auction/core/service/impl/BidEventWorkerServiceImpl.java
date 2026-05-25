package ws.academy.auction.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ws.academy.auction.core.dto.BidMessage;
import ws.academy.auction.core.enrichers.BidEnricher;
import ws.academy.auction.core.entity.Bid;
import ws.academy.auction.core.repository.BidRepository;
import ws.academy.auction.core.repository.LotRepository;
import ws.academy.auction.core.service.BidEventWorkerService;

@Service
@RequiredArgsConstructor
public class BidEventWorkerServiceImpl implements BidEventWorkerService {
    private final BidEnricher bidEnricher;
    private final LotRepository lotRepository;
    private final BidRepository bidRepository;

    @Override
    public Bid workBidEvent(BidMessage bidMessage) {
        Bid bid = bidEnricher.buildBid(bidMessage);
        bid.getAuctionLot().getLot().setBuyer(bid.getBuyer());
        bidRepository.save(bid);
        lotRepository.save(bid.getAuctionLot().getLot());
        return bid;
    }
}
