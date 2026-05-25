package ws.academy.auction.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ws.academy.auction.api.dto.AuctionStatus;
import ws.academy.auction.api.dto.rq.auctions.AuctionCreateRq;
import ws.academy.auction.api.dto.rq.auctions.AuctionSearchRequest;
import ws.academy.auction.api.dto.rs.GuidRs;
import ws.academy.auction.api.dto.rs.ListPageData;
import ws.academy.auction.api.dto.rs.auctions.*;
import ws.academy.auction.core.enrichers.impl.AuctionEnricherImpl;
import ws.academy.auction.core.entity.*;
import ws.academy.auction.core.enums.LotStatus;
import ws.academy.auction.core.helpers.AuctionHelper;
import ws.academy.auction.core.helpers.AuctionLotHelper;
import ws.academy.auction.core.helpers.LotHelper;
import ws.academy.auction.core.mapper.AuctionMapper;
import ws.academy.auction.core.repository.*;
import ws.academy.auction.core.service.AuctionService;
import ws.academy.auction.core.jpaspecifications.AuctionSpecification;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionHelper auctionHelper;
    private final AuctionLotHelper auctionLotHelper;
    private final AuctionEnricherImpl auctionEnricherImpl;
    private final LotHelper lotHelper;
    private final AuctionRepository auctionRepository;
    private final LotRepository lotRepository;
    private final AuctionLotRepository auctionLotRepository;
    private final ParticipantAuctionRepository participantAuctionRepository;
    private final AuctionMapper auctionMapper;

    @Override
    public GuidRs createAuction(AuctionCreateRq request) {
        Auction auction = new Auction();
        prepareNewAuction(auction, request);
        Auction savedAuction = auctionRepository.save(auction);
        return new GuidRs(savedAuction.getGuid());
    }

    @Override
    public void updateAuction(AuctionCreateRq request, UUID guid) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        updateAuctionDetails(auction, request);
        auctionRepository.save(auction);
    }

    @Override
    public void deleteAuction(UUID guid) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        if (auction.getStatus().equals(AuctionStatus.PUBLISHED)) {
            auctionRepository.delete(auction);
        }
        throw new IllegalArgumentException("Удалить можно только опубликованный аукцион");
    }

    @Override
    public AuctionRs getAuctionWithId(UUID guid) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        return auctionEnricherImpl.toAuctionDto(auction);
    }

    @Override
    public void actionAuction(UUID guid, String action) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        validateActionAuction(action, auction);
        auction.setStatus(resolveAuctionAction(action));
        if (action.equals("start")) {
            return;
        }
        auction.getAuctionLot().forEach(auctionLot -> {
            auctionLot.getLot().setLotStatus(LotStatus.SOLD);
            auctionLot.getBid().forEach(bid -> bid.setEndAt(LocalDateTime.now()));
        });
    }

    @Override
    public void setSellerLot(UUID guid, UUID lotGuid) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        Lot lot = lotHelper.getLotOrThrow(lotGuid);

        validateLotEligibilityForAuction(auction, lot);
        lot.setLotStatus(LotStatus.ON_TRADE);
        lotRepository.save(lot);
    }

    @Override
    public AuctionList getAuctions(AuctionSearchRequest request) {
        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getCount() != null ? request.getCount() : 30;

        Pageable pageable = getPageable(page, size);

        Specification<Auction> spec = getSpecification(request);

        Page<Auction> auctionPage = auctionRepository.findAll(spec, pageable);

        List<AuctionListItem> auctionListItems = createAuctionListItem(auctionPage);

        ListPageData listPageData = getPageData(size, page, auctionListItems.size());

        return createAuctionList(auctionListItems, listPageData);
    }

    private void prepareNewAuction(Auction auction, AuctionCreateRq request) {
        auction.setStartDate(request.getStartDate().atStartOfDay());
        auction.setStatus(AuctionStatus.PUBLISHED);
        Integer lastNumber = auctionRepository.findMaxNumber();
        auction.setNumber(lastNumber != null ? lastNumber + 1 : 1);
    }

    private void updateAuctionDetails(Auction auction, AuctionCreateRq request) {
        auction.setStartDate(request.getStartDate().atStartOfDay());
    }

    private AuctionStatus resolveAuctionAction(String action) {
        return switch (action.toLowerCase()) {
            case "start" -> AuctionStatus.ACTIVE;
            case "finish" -> AuctionStatus.ARCHIVE;
            default -> throw new IllegalArgumentException("Некорректное действие: " + action);
        };
    }

    private void validateActionAuction(String action, Auction auction) {
        if (auction.getStatus().equals(AuctionStatus.ACTIVE) && action.equals("start")) {
            throw new IllegalArgumentException("Торги уже начаты!");
        }
        if (auction.getStatus().equals(AuctionStatus.PUBLISHED)) {
            auction.setStatus(resolveAuctionAction(action));
        }
    }

    private void validateLotEligibilityForAuction(Auction auction, Lot lot) {
        auctionLotHelper.getAuctionLotOrThrow(auction, lot);
    }

    private List<AuctionListItem> createAuctionListItem(Page<Auction> auctionPage) {
        return auctionPage.getContent().stream()
                .map(this::getAuctionListItem)
                .toList();
    }

    private AuctionListItem getAuctionListItem(Auction auction) {
        AuctionListItem item = auctionMapper.toAuctionListItem(auction);
        item.setStartDate(auction.getStartDate());
        item.setLotsCount(auctionLotRepository.countByAuctionGuid(auction.getGuid()));
        item.setParticipantCount(participantAuctionRepository.countByAuctionGuid(auction.getGuid()));
        item.setBudget(auctionLotRepository.summaryCostLotsByAuctionGuid(auction.getGuid()));
        if (auction.getStatus() == AuctionStatus.ACTIVE) {
            item.setParticipation(true);
        }
        item.setActions(List.of("DELETE"));
        return item;
    }

    private ListPageData getPageData(int size, int page, int items) {
        return ListPageData.builder()
                .totalPages(size)
                .page(page + 1)
                .size(items)
                .build();
    }

    private Specification<Auction> getSpecification(AuctionSearchRequest request) {
        return Specification
                .where(AuctionSpecification.hasParticipants(request.getParticipants()))
                .and(AuctionSpecification.hasStatuses(request.getStatuses()))
                .and(AuctionSpecification.hasFrom(request.getFrom().atStartOfDay()))
                .and(AuctionSpecification.hasTo(request.getTo().atTime(LocalTime.MAX)));
    }

    private Pageable getPageable(int page, int size) {
        return PageRequest.of(page, size);
    }

    private AuctionList createAuctionList(List<AuctionListItem> auctionListItems, ListPageData listPageData) {
        return AuctionList.builder()
                .items(auctionListItems)
                .page(listPageData)
                .build();
    }
}
