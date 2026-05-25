package ws.academy.auction.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RestController;
import ws.academy.auction.api.controller.AuctionController;
import ws.academy.auction.api.dto.Bidding;
import ws.academy.auction.api.dto.BiddingBoard;
import ws.academy.auction.api.dto.LotBid;
import ws.academy.auction.api.dto.rq.auctions.AuctionCreateRq;
import ws.academy.auction.api.dto.rq.auctions.AuctionLotsSearchRequest;
import ws.academy.auction.api.dto.rq.auctions.AuctionSearchRequest;
import ws.academy.auction.api.dto.rq.bids.CreateBidRq;
import ws.academy.auction.api.dto.rq.lots.LotGuidRq;
import ws.academy.auction.api.dto.rq.participantAuctions.ParticipantAuctionSearchRq;
import ws.academy.auction.api.dto.rq.participants.ParticipantSearchRq;
import ws.academy.auction.api.dto.rq.participants.SubmittingRq;
import ws.academy.auction.api.dto.rs.GuidRs;
import ws.academy.auction.api.dto.rs.auctions.AuctionLotList;
import ws.academy.auction.api.dto.rs.auctions.AuctionRs;
import ws.academy.auction.api.dto.rs.auctions.AuctionList;
import ws.academy.auction.api.dto.rs.bids.BidList;
import ws.academy.auction.api.dto.rs.bids.BidStatusRs;
import ws.academy.auction.api.dto.rs.participantAuctions.ListParticipantAuctionRs;
import ws.academy.auction.api.dto.rs.participants.ParticipantAuctionDetails;
import ws.academy.auction.api.dto.rs.participants.ParticipantListRs;
import ws.academy.auction.core.dto.CreateBidRs;
import ws.academy.auction.core.service.AuctionLotService;
import ws.academy.auction.core.service.AuctionService;
import ws.academy.auction.core.service.BidService;
import ws.academy.auction.core.service.ParticipantAuctionService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AuctionControllerImpl implements AuctionController {

    private final AuctionService auctionService;
    private final AuctionLotService auctionLotService;
    private final ParticipantAuctionService participantAuctionService;
    private final BidService bidService;

    @Override
    public GuidRs createAuction(AuctionCreateRq startDate) {
        return auctionService.createAuction(startDate);
    }

    @Override
    public void updateAuction(AuctionCreateRq startDate, UUID guid) {
        auctionService.updateAuction(startDate, guid);
    }

    @Override
    public void deleteAuction(UUID guid) {
        auctionService.deleteAuction(guid);
    }

    @Override
    public AuctionRs getAuctionWithId(UUID guid) {
        return auctionService.getAuctionWithId(guid);
    }

    @Override
    public void actionAuction(UUID guid, String action) {
        auctionService.actionAuction(guid, action);
    }

    @Override
    public AuctionLotList getAuctionLots(UUID guid, AuctionLotsSearchRequest request) {
        return auctionLotService.getLotsByAuctionId(guid, request);
    }

    @Override
    public void addAuctionLot(UUID guid, LotGuidRq lotGuid) {
        auctionLotService.addAuctionLot(guid, lotGuid);
    }

    @Override
    public void addAuctionLotAdmin(UUID guid, UUID lotGuid) {
        auctionLotService.addAuctionLot(guid, lotGuid);
    }

    @Override
    public void deleteAuctionLot(UUID guid, LotGuidRq lotGuid) {
        auctionLotService.deleteAuctionLot(guid, lotGuid);
    }

    @Override
    public void deleteAuctionLotAdmin(UUID guid, UUID lotGuid) {
        auctionLotService.deleteAuctionLot(guid, lotGuid);
    }

    @Override
    public Bidding getBids(Integer id, Integer lotId) {
        return null;
    }

    @Override
    public Bidding placeBid(Integer id, Integer lotId, LotBid lotBid) {
        return Bidding.builder().build();
    }

    @Override
    public GuidRs submittingAnAuction(UUID guid, SubmittingRq request) {
        return participantAuctionService.submittingAnAuction(guid, request);
    }

    @Override
    public ParticipantListRs getParticipants(UUID guid, ParticipantSearchRq request) {
        return participantAuctionService.getParticipants(guid, request);
    }

    @Override
    public ParticipantAuctionDetails getParticipantAuctionDetails(UUID guid, UUID participantGuid) {
        return participantAuctionService.getParticipantAuction(guid, participantGuid);
    }

    @Override
    public void deleteParticipantAuctionDetails(UUID guid, UUID participantGuid) {
        participantAuctionService.deleteParticipantAuction(guid, participantGuid);
    }

    @Override
    public void submittingAnAuction(UUID guid) {
        participantAuctionService.submittingAnAuction(guid);
    }

    @Override
    public void actionSubmittingAnAuction(UUID guid, String action, UUID participantGuid) {
        participantAuctionService.actionSubmittingAnAuction(guid, action, participantGuid);
    }

    @Override
    public void setSellerLot(UUID guid, UUID lotGuid) {
        auctionService.setSellerLot(guid, lotGuid);
    }

    @Override
    public ListParticipantAuctionRs getParticipantAuctions(UUID guid, ParticipantAuctionSearchRq request) {
        return participantAuctionService.getParticipantAuctions(guid, request);
    }

    @Override
    public AuctionList getAuctions(AuctionSearchRequest request) {
        return auctionService.getAuctions(request);
    }

    @Override
    public BidStatusRs addBidLot(UUID auctionId, UUID lotId, CreateBidRq request) {
        CreateBidRs rs = bidService.addLotBid(auctionId, lotId, request);
        return new BidStatusRs(rs.getStatus(), rs.getGuid());
    }

    @Override
    public BidList getBids(UUID auctionId, UUID lotId) {
        return bidService.getBids(auctionId, lotId);
    }

    @Override
    @Cacheable(value = "BiddingBoard")
    public BiddingBoard getBiddingBoard(Integer id) {
        return null;
    }
}
