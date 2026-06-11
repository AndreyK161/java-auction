package ws.academy.auction.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ws.academy.auction.api.dto.rq.bids.BidRq;
import ws.academy.auction.api.dto.rq.bids.CreateBidRq;
import ws.academy.auction.api.dto.rs.auctions.CurrentAuctionRs;
import ws.academy.auction.api.dto.rs.bids.BidList;
import ws.academy.auction.core.dto.BidStatusMessage;
import ws.academy.auction.core.dto.CreateBidRs;
import ws.academy.auction.core.enrichers.BidEnricher;
import ws.academy.auction.core.entity.*;
import ws.academy.auction.core.enums.BidStatus;
import ws.academy.auction.core.exception.InvalidBidValidationException;
import ws.academy.auction.core.helpers.AuctionHelper;
import ws.academy.auction.core.helpers.AuctionLotHelper;
import ws.academy.auction.core.helpers.LotHelper;
import ws.academy.auction.core.helpers.ParticipantHelper;
import ws.academy.auction.core.producer.BidEventProducer;
import ws.academy.auction.core.repository.BidRepository;
import ws.academy.auction.core.repository.ParticipantAuctionRepository;
import ws.academy.auction.core.service.BidService;
import ws.academy.auction.core.messages.BidResultGateway;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
    private final AuctionHelper auctionHelper;
    private final ParticipantHelper participantHelper;
    private final AuctionLotHelper auctionLotHelper;
    private final BidRepository bidRepository;
    private final LotHelper lotHelper;
    private final BidEventProducer bidEventProducer;
    private final BidResultGateway bidResultGateway;
    private final BidEnricher bidEnricher;
    private final ParticipantAuctionRepository participantAuctionRepository;

    @Override
    public CreateBidRs addLotBid(UUID auctionId, UUID lotId, CreateBidRq request) {
        try {
            Lot lot = lotHelper.getLotOrThrow(lotId);
            Auction auction = auctionHelper.getAuctionOrThrow(auctionId);
            AuctionLot auctionLot = auctionLotHelper.getAuctionLotOrThrow(auction, lot);

            if (request.getParticipantNumber() == null && request.getParticipant() != null) {
                Participant participant = participantHelper.getParticipantOrThrow(request.getParticipant().getGuid());
                participantAuctionRepository.findAuctionParticipantByAuctionAndParticipant(auction, participant)
                        .ifPresent(pa -> request.setParticipantNumber(pa.getParticipantNumber()));
            }

            validateBid(request, getMinimumAllowed(auctionLot));

            changeAllOldActiveStatuses(auctionLot);
            return sendBidAndAwaitResponse(auctionLot, request);
        } catch (InvalidBidValidationException e) {
            return new CreateBidRs(null, BidStatus.REJECTED.getName());
        }
    }

    @Override
    public BidList getBids(UUID auctionId, UUID lotId) {
        Lot lot = lotHelper.getLotOrThrow(lotId);
        Auction auction = auctionHelper.getAuctionOrThrow(auctionId);
        AuctionLot auctionLot = auctionLotHelper.getAuctionLotOrThrow(auction, lot);
        int bidCount = bidRepository.countBidsByAuctionLot(auctionLot);

        List<Bid> lotBids = bidRepository.findAllByAuctionLotOrderByStartAtDesc(auctionLot);

        List<BidRq> items = getBidItems(lotBids);

        CurrentAuctionRs currentAuctionRs = getCurrentAuctionRs(items, bidCount);

        return createBidList(currentAuctionRs, items);
    }

    private CreateBidRs sendBidAndAwaitResponse(AuctionLot auctionLot, CreateBidRq request) {
        UUID bidGuid = UUID.randomUUID();
        CompletableFuture<BidStatusMessage> future = new CompletableFuture<>();
        bidResultGateway.register(bidGuid, future);

        bidEventProducer.sendBidEvent(bidEnricher.buildBidMessage(auctionLot, request, bidGuid));
        try {
            BidStatusMessage result = future.get(5, TimeUnit.SECONDS);
            return new CreateBidRs(result.getGuid(), result.getStatus());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private BigDecimal getMinimumAllowed(AuctionLot auctionLot) {
        Integer maxBuyerNumber = bidRepository.findMaxNumber(auctionLot);
        BigDecimal lastBidAmount = getLastBidAmount(maxBuyerNumber, auctionLot, auctionLot.getLot());
        return lastBidAmount.add(auctionLot.getLot().getPriceStep());
    }

    private CurrentAuctionRs getCurrentAuctionRs(List<BidRq> items, int bidCount) {
        CurrentAuctionRs currentAuctionRs = buildCurrentAuctionRs(items);

        items.stream().max(Comparator.comparing(BidRq::getAmount))
                .ifPresent(maxBid -> currentAuctionRs.setLeader(maxBid.getParticipant()));
        currentAuctionRs.setBidCount(bidCount);
        return currentAuctionRs;
    }

    private List<BidRq> getBidItems(List<Bid> lotBids) {
        return lotBids.stream()
                .map(this::getBidRq)
                .collect(Collectors.toList());
    }

    private BidRq getBidRq(Bid bid) {
        return bidEnricher.buildBidRq(bid);
    }

    private BidList createBidList(CurrentAuctionRs currentAuctionRs, List<BidRq> items) {
        return BidList.builder()
                .trade(currentAuctionRs)
                .items(items)
                .build();
    }

    private CurrentAuctionRs buildCurrentAuctionRs(List<BidRq> items) {
        return CurrentAuctionRs.builder()
                .startAt(items.isEmpty() ? null : items.get(items.size() - 1).getDateTime())
                .endAt(items.isEmpty() ? null : items.get(0).getDateTime())
                .build();
    }

    private void validateBid(CreateBidRq request, BigDecimal minimumAllowed) {
        Participant participant = participantHelper.getParticipantOrThrow(request.getParticipant().getGuid());
        if (request.getAmount().compareTo(participant.getBalance()) > 0) {
            throw new InvalidBidValidationException("Ставка не может быть выше чем баланс участника");
        }

        if (request.getAmount().compareTo(minimumAllowed) <= 0) {
            throw new InvalidBidValidationException("Ставка должна быть выше текущей + шаг");
        }
    }

    private BigDecimal getLastBidAmount(Integer maxBidNumber, AuctionLot auctionLot, Lot lot) {
        return bidRepository.findByBidNumberAndAuctionLot(maxBidNumber, auctionLot)
                .map(Bid::getAmount)
                .orElse(lot.getStartPrice());
    }

    private void changeAllOldActiveStatuses(AuctionLot auctionLot) {
        bidRepository.findAllByAuctionLotAndBidStatus(auctionLot, BidStatus.ACCEPTED)
                .forEach(bid -> bid.setBidStatus(BidStatus.OUTBID));
    }
}
