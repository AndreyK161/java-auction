package ws.academy.auction.core.enrichers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.academy.auction.api.dto.rs.auctions.AuctionRs;
import ws.academy.auction.api.dto.rs.lots.AuctionLotRs;
import ws.academy.auction.core.enrichers.AuctionEnricher;
import ws.academy.auction.core.enrichers.BidEnricher;
import ws.academy.auction.core.entity.*;
import ws.academy.auction.core.helpers.AuctionLotHelper;
import ws.academy.auction.core.helpers.BidHelper;
import ws.academy.auction.core.helpers.ParticipantAuctionHelper;
import ws.academy.auction.core.mapper.AuctionMapper;
import ws.academy.auction.core.mapper.PhotoMapper;
import ws.academy.auction.core.repository.BidRepository;
import ws.academy.auction.core.repository.PhotoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuctionEnricherImpl implements AuctionEnricher {
    private final BidHelper bidHelper;
    private final AuctionLotHelper auctionLotHelper;
    private final ParticipantAuctionHelper participantAuctionHelper;
    private final AuctionMapper auctionMapper;
    private final ParticipantAuctionEnricherImpl participantAuctionEnricher;
    private final BidEnricher bidEnricher;
    private final BidRepository bidRepository;
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    @Override
    public AuctionRs toAuctionDto(Auction auction) {
        AuctionRs dto = auctionMapper.buildAuctionRs(auction);
        enrichAuctionDto(dto, auction);
        return dto;
    }

    @Override
    public List<AuctionLotRs> getTradingLots(Auction auction) {
        return auction.getAuctionLot().stream()
                .map(AuctionLot::getLot)
                .map(lot -> {
                    AuctionLot auctionLot = auctionLotHelper.getAuctionLotOrThrow(auction, lot);
                    Integer maxBidNumber = bidRepository.findMaxNumber(auctionLot);
                    Optional<Bid> bidOpt = maxBidNumber != null
                            ? bidRepository.findByBidNumberAndAuctionLot(maxBidNumber, auctionLot)
                            : Optional.empty();
                    return bidOpt.map(bid -> {
                        ParticipantAuction pa = participantAuctionHelper
                                .getParticipantAuctionOrThrow(auction, bid.getBuyer());
                        return getAuctionLotRs(lot, bid, pa, auctionLot);
                    }).orElseGet(() -> getAuctionLotRsNoBid(lot, auctionLot));
                })
                .collect(Collectors.toList());
    }

    @Override
    public AuctionLotRs getAuctionLotRs(Lot lot, Bid bid, ParticipantAuction participantAuction,
                                         AuctionLot auctionLot) {
        return AuctionLotRs.builder()
                .guid(lot.getGuid())
                .title(lot.getTitle())
                .startPrice(lot.getStartPrice())
                .lastBid(bidEnricher.buildBidRs(bid, participantAuction))
                .bidCount(bidRepository.countBidsByAuctionLot(auctionLot))
                .status(lot.getLotStatus().name())
                .number(auctionLot.getLotNumber())
                .build();
    }

    private AuctionLotRs getAuctionLotRsNoBid(Lot lot, AuctionLot auctionLot) {
        return AuctionLotRs.builder()
                .guid(lot.getGuid())
                .title(lot.getTitle())
                .startPrice(lot.getStartPrice())
                .bidCount(bidRepository.countBidsByAuctionLot(auctionLot))
                .status(lot.getLotStatus().name())
                .number(auctionLot.getLotNumber())
                .build();
    }

    @Override
    public void setPhotos(AuctionRs auctionRs) {
        auctionRs.getTradingLots().forEach(lotShortRs -> {
            List<Photo> photos = photoRepository.findAllByEntityTypeAndEntityGuid("LOT", lotShortRs.getGuid());
            lotShortRs.setPhoto(
                    photos.stream()
                            .map(photoMapper::buildPhotoDetails)
                            .collect(Collectors.toList())
            );
        });
    }

    private void enrichAuctionDto(AuctionRs dto, Auction auction) {
        dto.setStartDate(auction.getStartDate());
        dto.setTradingLots(getTradingLots(auction));
        if (dto.getTradingLots() != null) {
            setPhotos(dto);
        }
        if (auction.getParticipantAuctionList() != null) {
            participantAuctionEnricher.setAuctionParticipantList(dto, auction);
        }
    }
}
