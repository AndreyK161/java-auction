package ws.academy.auction.core.helpers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.academy.auction.core.entity.Auction;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Lot;
import ws.academy.auction.core.enums.LotStatus;
import ws.academy.auction.core.exception.NotFoundException;
import ws.academy.auction.core.helpers.AuctionLotHelper;
import ws.academy.auction.core.repository.AuctionLotRepository;

@Component
@RequiredArgsConstructor
public class AuctionLotHelperImpl implements AuctionLotHelper {
    private final AuctionLotRepository auctionLotRepository;

    @Override
    public AuctionLot getAuctionLotOrThrow(Auction auction, Lot lot) {
        return auctionLotRepository.findByAuctionAndLot(auction, lot)
                .orElseThrow(() -> new NotFoundException("Указанный лот не участвует в данном аукционе"));
    }

    @Override
    public AuctionLot getAuctionLotOrThrowWithLot(Lot lot) {
        return auctionLotRepository.findByLot(lot)
                .orElseThrow(() -> new NotFoundException("Лот не зарегистрирован на торгах"));
    }

    @Override
    public AuctionLot getArchiveAuctionLotFor(Lot lot) {
        return auctionLotRepository.findByLotAndLot_LotStatus(lot, LotStatus.SOLD)
                .orElseThrow(() -> new NotFoundException("Торги еще не окончены"));
    }
}
