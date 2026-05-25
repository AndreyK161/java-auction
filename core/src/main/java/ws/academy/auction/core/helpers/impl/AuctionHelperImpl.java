package ws.academy.auction.core.helpers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.academy.auction.core.entity.Auction;
import ws.academy.auction.core.exception.NotFoundException;
import ws.academy.auction.core.repository.AuctionRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuctionHelperImpl implements ws.academy.auction.core.helpers.AuctionHelper {
    private final AuctionRepository auctionRepository;

    @Override
    public Auction getAuctionOrThrow(UUID guid) {
        return auctionRepository.findById(guid)
                .orElseThrow(() -> new NotFoundException("Аукцион не найден с id = " + guid));
    }
}
