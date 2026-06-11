package ws.academy.auction.core.enrichers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.academy.auction.api.dto.rs.auctions.AuctionLotListItem;
import ws.academy.auction.api.dto.rs.auctions.AuctionRs;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;
import ws.academy.auction.core.enrichers.AuctionLotEnricher;
import ws.academy.auction.core.enrichers.BidEnricher;
import ws.academy.auction.core.entity.*;
import ws.academy.auction.core.helpers.BidHelper;
import ws.academy.auction.core.helpers.ParticipantAuctionHelper;
import ws.academy.auction.core.mapper.AuctionLotMapper;
import ws.academy.auction.core.mapper.ParticipantMapper;
import ws.academy.auction.core.repository.BidRepository;
import ws.academy.auction.core.service.AuctionService;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuctionLotEnricherImpl implements AuctionLotEnricher {
    private final BidHelper bidHelper;
    private final ParticipantAuctionHelper participantAuctionHelper;
    private final ParticipantMapper participantMapper;
    private final LotEnricherImpl lotEnricher;
    private final BidEnricher bidEnricher;
    private final AuctionService auctionService;
    private final BidRepository bidRepository;
    private final AuctionLotMapper auctionLotMapper;

    @Override
    public AuctionLotListItem getAuctionLotListItem(AuctionLot auctionLot) {
        Lot lot = auctionLot.getLot();
        Integer maxBidNumber = bidRepository.findMaxNumber(auctionLot);
        Optional<Bid> bidOpt = maxBidNumber != null
                ? bidRepository.findByBidNumberAndAuctionLot(maxBidNumber, auctionLot)
                : Optional.empty();

        ParticipantDetails participantDetails = participantMapper.buildParticipantDetails(lot.getOwner());
        AuctionRs auctionRs = auctionService.getAuctionWithId(auctionLot.getAuction().getGuid());

        AuctionLotListItem.AuctionLotListItemBuilder builder = AuctionLotListItem.builder()
                .lot(lotEnricher.bindLotWithDetails(lot, participantDetails, auctionRs))
                .status(lot.getLotStatus().getStatusName())
                .number(auctionLot.getAuction().getNumber())
                .winner(participantMapper.buildParticipantDetails(lot.getBuyer()))
                .totalAmount(bidRepository.sumBidsByAuctionLot(auctionLot))
                .actions(List.of("DELETE"));

        bidOpt.ifPresent(bid -> {
            ParticipantAuction participantAuction = participantAuctionHelper
                    .getParticipantAuctionOrThrow(auctionLot.getAuction(), bid.getBuyer());
            builder.lastBid(bidEnricher.buildBidRs(bid, participantAuction));
        });

        return builder.build();
    }

    @Override
    public AuctionLot buildAuctionLot(Auction auction, Lot lot) {
        return auctionLotMapper.buildAuctionLot(auction, lot);
    }
}
