package ws.academy.auction.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ws.academy.auction.api.dto.rq.auctions.AuctionLotsSearchRequest;
import ws.academy.auction.api.dto.rq.lots.LotGuidRq;
import ws.academy.auction.api.dto.rs.ListPageData;
import ws.academy.auction.api.dto.rs.auctions.AuctionLotList;
import ws.academy.auction.api.dto.rs.auctions.AuctionLotListItem;
import ws.academy.auction.core.enrichers.impl.AuctionLotEnricherImpl;
import ws.academy.auction.core.entity.*;
import ws.academy.auction.core.enums.LotSortField;
import ws.academy.auction.core.enums.LotStatus;
import ws.academy.auction.core.exception.NotFoundException;
import ws.academy.auction.core.helpers.AuctionHelper;
import ws.academy.auction.core.helpers.AuctionLotHelper;
import ws.academy.auction.core.helpers.LotHelper;
import ws.academy.auction.core.jpaspecifications.AuctionLotSpecification;
import ws.academy.auction.core.mapper.LotMapper;
import ws.academy.auction.core.repository.*;
import ws.academy.auction.core.service.AuctionLotService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuctionLotServiceImpl implements AuctionLotService {
    private final AuctionHelper auctionHelper;
    private final AuctionLotHelper auctionLotHelper;
    private final LotHelper lotHelper;
    private final AuctionLotEnricherImpl auctionLotEnricher;
    private final LotRepository lotRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionLotRepository auctionLotRepository;
    private final LotMapper lotMapper;

    @Override
    public AuctionLotList getLotsByAuctionId(UUID guid, AuctionLotsSearchRequest request) {
        validateAuctionExists(guid);

        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getCount() != null ? request.getCount() : 30;

        String lotStatus = request.getStatus() != null ? request.getStatus() : "NEW";
        LotSortField sortField = LotSortField.from(request.getSort());
        Sort.Direction direction = "desc".equalsIgnoreCase(request.getSort()) ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = getPageable(page, size, sortField.getEntityField(), direction);

        Specification<AuctionLot> spec = getSpecification(guid, lotStatus);

        Page<AuctionLot> auctionLotPage = auctionLotRepository.findAll(spec, pageable);

        List<AuctionLotListItem> items = createAuctionListItem(auctionLotPage);

        ListPageData pageData = createListPageData(request, size, auctionLotPage);

        return createAuctionList(items, pageData);
    }

    @Override
    public void addAuctionLot(UUID guid, LotGuidRq lotGuid) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        Lot lot = lotHelper.getLotOrThrow(lotGuid.getLot());
        AuctionLot auctionLot = auctionLotEnricher.buildAuctionLot(auction, lot);

        auctionLot.setLotNumber(Optional.ofNullable(auctionLotRepository.findMaxNumber(auction)).orElse(0) + 1);
        lot.setLotStatus(LotStatus.AUCTION_REQUEST);

        auctionLotRepository.save(auctionLot);
        lotRepository.save(lot);
    }

    @Override
    public void deleteAuctionLot(UUID guid, LotGuidRq lotGuid) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        Lot lot = lotHelper.getLotOrThrow(lotGuid.getLot());
        AuctionLot auctionLot = auctionLotHelper.getAuctionLotOrThrow(auction, lot);
        auctionLotRepository.delete(auctionLot);
    }

    @Override
    public void addAuctionLot(UUID guid, UUID lotGuid) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        Lot lot = lotHelper.getLotOrThrow(lotGuid);
        AuctionLot auctionLot = auctionLotEnricher.buildAuctionLot(auction, lot);

        lot.setLotStatus(LotStatus.AUCTION_REQUEST);

        lotRepository.save(lot);
        auctionLotRepository.save(auctionLot);
    }

    @Override
    public void deleteAuctionLot(UUID guid, UUID lotGuid) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        Lot lot = lotHelper.getLotOrThrow(lotGuid);
        AuctionLot auctionLot = auctionLotHelper.getAuctionLotOrThrow(auction, lot);
        auctionLotRepository.delete(auctionLot);
    }

    private AuctionLotList createAuctionList(List<AuctionLotListItem> auctionLotListItems, ListPageData pageData) {
        return AuctionLotList.builder()
                .items(auctionLotListItems)
                .page(pageData)
                .build();
    }

    private ListPageData createListPageData(AuctionLotsSearchRequest request, int size,
                                            Page<AuctionLot> auctionLotPage) {
        return lotMapper.buildListPageData(request, size, auctionLotPage);
    }

    private List<AuctionLotListItem> createAuctionListItem(Page<AuctionLot> lotPage) {
        return lotPage.getContent().stream()
                .map(auctionLotEnricher::getAuctionLotListItem)
                .toList();
    }

    private Pageable getPageable(int page, int size, String sort, Sort.Direction direction) {
        return PageRequest.of(page, size, Sort.by(direction, sort));
    }

    private Specification<AuctionLot> getSpecification(UUID guid, String status) {
        return Specification
                .where(AuctionLotSpecification.hasAuctionGuid(guid))
                .and(AuctionLotSpecification.hasAuctionStatus(LotStatus.valueOf(status)));
    }

    private void validateAuctionExists(UUID guid) {
        if (!auctionRepository.existsById(guid)) {
            throw new NotFoundException("Аукцион не найден с id = " + guid);
        }
    }
}
