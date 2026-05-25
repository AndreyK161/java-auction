package ws.academy.auction.core.enrichers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.academy.auction.api.dto.rq.bids.BidRq;
import ws.academy.auction.api.dto.rq.bids.CreateBidRq;
import ws.academy.auction.api.dto.rs.bids.BidRs;
import ws.academy.auction.core.dto.BidMessage;
import ws.academy.auction.core.enrichers.BidEnricher;
import ws.academy.auction.core.entity.*;
import ws.academy.auction.core.enums.BidStatus;
import ws.academy.auction.core.helpers.*;
import ws.academy.auction.core.mapper.ParticipantMapper;
import ws.academy.auction.core.repository.BidRepository;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BidEnricherImpl implements BidEnricher {
    private final AuctionHelper auctionHelper;
    private final LotHelper lotHelper;
    private final AuctionLotHelper auctionLotHelper;
    private final ParticipantHelper participantHelper;
    private final ParticipantMapper participantMapper;
    private final BidRepository bidRepository;

    @Override
    public BidRs buildBidRs(Bid bid, ParticipantAuction participantAuction) {
        return BidRs.builder()
                .dateTime(bid.getEndAt())
                .amount(bid.getAmount())
                .participantNumber(participantAuction.getParticipantNumber())
                .participant(participantMapper.buildParticipantDetails(bid.getBuyer()))
                .build();
    }

    @Override
    public BidRq buildBidRq(Bid bid) {
        return BidRq.builder()
                .dateTime(bid.getEndAt())
                .amount(bid.getAmount())
                .participantNumber(getParticipantNumber(bid))
                .participant(participantMapper.buildParticipantDetails(bid.getBuyer()))
                .build();
    }

    @Override
    public Bid buildBid(BidMessage bidMessage) {
        Auction auction = auctionHelper.getAuctionOrThrow(bidMessage.getAuctionId());
        Lot lot = lotHelper.getLotOrThrow(bidMessage.getLotId());
        AuctionLot auctionLot = auctionLotHelper.getAuctionLotOrThrow(auction, lot);
        Participant participant = participantHelper.getParticipantOrThrow(bidMessage.getParticipantId());
        return Bid.builder()
                .guid(bidMessage.getGuid())
                .auctionLot(auctionLotHelper.getAuctionLotOrThrow(auction, lot))
                .buyer(participant)
                .amount(bidMessage.getAmount())
                .bidStatus(BidStatus.ACCEPTED)
                .bidNumber(Optional.ofNullable(bidRepository.findMaxNumber(auctionLot)).orElse(0) + 1)
                .startAt(bidMessage.getDateTime())
                .build();
    }

    @Override
    public BidMessage buildBidMessage(AuctionLot auctionLot, CreateBidRq request, UUID bidGuid) {
        return BidMessage.builder()
                .guid(bidGuid)
                .auctionId(auctionLot.getAuction().getGuid())
                .lotId(auctionLot.getLot().getGuid())
                .amount(request.getAmount())
                .participantId(request.getParticipant().getGuid())
                .participantNumber(request.getParticipantNumber())
                .dateTime(request.getDateTime())
                .build();
    }

    private Integer getParticipantNumber(Bid bid) {
        return bid.getAuctionLot().getAuction()
                .getParticipantAuctionList().stream()
                .filter(p -> p.getParticipant().equals(bid.getBuyer()))
                .findFirst().map(ParticipantAuction::getParticipantNumber)
                .orElse(0);
    }
}
