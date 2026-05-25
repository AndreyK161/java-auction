package ws.academy.auction.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ws.academy.auction.api.dto.rq.lots.CreateLotRq;
import ws.academy.auction.api.dto.rq.lots.LotSearchRequest;
import ws.academy.auction.api.dto.rq.lots.UpdateLotRq;
import ws.academy.auction.api.dto.rq.photos.LotPhotoUUIDRq;
import ws.academy.auction.api.dto.rs.ListPageData;
import ws.academy.auction.api.dto.rs.auctions.AuctionRs;
import ws.academy.auction.api.dto.rs.lots.LotDetails;
import ws.academy.auction.api.dto.rs.lots.LotList;
import ws.academy.auction.api.dto.rs.lots.LotListItem;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;
import ws.academy.auction.core.enrichers.LotEnricher;
import ws.academy.auction.core.enrichers.ParticipantEnricher;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Lot;
import ws.academy.auction.core.entity.Participant;
import ws.academy.auction.core.entity.Photo;
import ws.academy.auction.core.enums.LotStatus;
import ws.academy.auction.core.exception.NotFoundException;
import ws.academy.auction.core.helpers.LotHelper;
import ws.academy.auction.core.helpers.ParticipantHelper;
import ws.academy.auction.core.helpers.PhotoHelper;
import ws.academy.auction.core.mapper.LotMapper;
import ws.academy.auction.core.repository.AuctionLotRepository;
import ws.academy.auction.core.repository.LotRepository;
import ws.academy.auction.core.repository.PhotoRepository;
import ws.academy.auction.core.service.AuctionService;
import ws.academy.auction.core.service.LotService;
import ws.academy.auction.core.jpaspecifications.LotSpecification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LotServiceImpl implements LotService {
    private final ParticipantHelper participantHelper;
    private final LotHelper lotHelper;
    private final PhotoHelper photoHelper;
    private final LotRepository lotRepository;
    private final PhotoRepository photoRepository;
    private final AuctionLotRepository auctionLotRepository;
    private final LotMapper lotMapper;
    private final LotEnricher lotEnricher;
    private final ParticipantEnricher participantEnricher;
    private final AuctionService auctionService;

    @Override
    public LotDetails createLot(CreateLotRq createLotRq) {
        Lot lot = lotMapper.buildLot(createLotRq);
        lot.setCreatedAt(LocalDateTime.now());
        lot.setLotStatus(LotStatus.NEW);

        Participant owner = participantHelper.getParticipantOrThrow(createLotRq.getOwner().getGuid());

        Lot savedLot = lotRepository.save(lot);

        if (createLotRq.getPhoto() == null) {
            createLotRq.setPhoto(new ArrayList<>());
        }

        List<Photo> photos = loadPhotosByIds(
                createLotRq.getPhoto().stream()
                        .map(LotPhotoUUIDRq::getGuid)
                        .collect(Collectors.toList())
        );

        setPhotos(photos, savedLot.getGuid());

        lot.setOwner(owner);

        ParticipantDetails participant = participantEnricher.toParticipantDto(owner);

        return lotEnricher.bindLotWithDetails(savedLot, participant, null);
    }

    @Override
    public LotDetails getLotById(UUID guid) {
        Lot lot = lotHelper.getLotOrThrow(guid);
        Participant participant = participantHelper.getParticipantOrThrow(lot.getOwner().getGuid());
        ParticipantDetails participantDto = participantEnricher.toParticipantDto(participant);
        AuctionLot auctionLot = auctionLotRepository.findByLot(lot).orElse(null);
        if (auctionLot != null) {
            AuctionRs auctionRs = auctionService.getAuctionWithId(auctionLot.getAuction().getGuid());
            return lotEnricher.bindLotWithDetails(lot, participantDto, auctionRs);
        }

        return lotEnricher.bindLotWithDetails(lot, participantDto, null);
    }

    @Override
    public LotList getLots(LotSearchRequest request) {
        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getCount() != null ? request.getCount() : 30;
        boolean deleted = request.getShowDeleted().equals("Y");

        Pageable pageable = getPageable(page, size, request);

        Specification<Lot> spec = getSpecification(request, deleted);

        Page<Lot> lotPage = lotRepository.findAll(spec, pageable);

        List<LotListItem> lotListItems = createLotListItem(lotPage);

        ListPageData pageData = createListPageData(request, size, lotPage);

        return createLotList(lotListItems, pageData);
    }

    @Override
    public LotDetails updateLot(UpdateLotRq updateLotRq, UUID guid) {
        Lot lot = lotHelper.getLotOrThrow(guid);
        Participant participant = participantHelper.getParticipantOrThrow(lot.getOwner().getGuid());
        ParticipantDetails participantDto = participantEnricher.toParticipantDto(participant);
        AuctionLot auctionLot = auctionLotRepository.findByLot(lot).orElse(null);

        if (updateLotRq.getPhoto() == null) {
            updateLotRq.setPhoto(new ArrayList<>());
        }

        List<Photo> photos = loadPhotosByIds(
                updateLotRq.getPhoto().stream()
                        .map(LotPhotoUUIDRq::getGuid)
                        .collect(Collectors.toList()));

        lot.setTitle(updateLotRq.getTitle());
        lot.setDescription(updateLotRq.getDescription());
        lot.setStartPrice(updateLotRq.getStartPrice());

        photoRepository.deleteAllByEntityTypeAndGuid("LOT", guid);

        setPhotos(photos, guid);

        if (auctionLot != null) {
            AuctionRs auctionRs = auctionService.getAuctionWithId(auctionLot.getAuction().getGuid());
            return lotEnricher.bindLotWithDetails(lot, participantDto, auctionRs);
        }
        return lotEnricher.bindLotWithDetails(lot, participantDto, null);
    }

    @Override
    public void deleteLot(UUID guid) {
        if (lotRepository.findByGuid(guid).isEmpty()) {
            throw new NotFoundException("Лот не найден с переданным: id = " + guid);
        }
        lotRepository.delete(lotHelper.getLotOrThrow(guid));
    }

    private void setPhotos(List<Photo> photos, UUID guid) {
        photos.forEach(photo -> {
            photo.setEntityType("LOT");
            photo.setEntityGuid(guid);
        });
    }

    private List<Photo> loadPhotosByIds(List<UUID> photoIds) {
        if (photoIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<Photo> photos = photoRepository.findAllById(photoIds);
        if (photos.size() != photoIds.size()) {
            throw new NotFoundException("Некоторые фотографии не найдены");
        }
        return photos;
    }

    private LotList createLotList(List<LotListItem> lotListItems, ListPageData pageData) {
        return LotList.builder()
                .items(lotListItems)
                .page(pageData)
                .build();
    }

    private ListPageData createListPageData(LotSearchRequest request, int size, Page<Lot> lotPage) {
        return lotMapper.buildListPageData(request, size, lotPage);
    }

    private List<LotListItem> createLotListItem(Page<Lot> lotPage) {
        return lotPage.getContent().stream()
                .map(lot -> {
                    LotListItem item = lotMapper.toLotListItem(lot);
                    item.setPhotos(photoHelper.getPhotoDetailsList(lot));
                    item.setCreatedAt(lot.getCreatedAt());
                    return item;
                })
                .toList();
    }

    private Pageable getPageable(int page, int size, LotSearchRequest request) {
        return PageRequest.of(page, size, Sort.by
                (Sort.Direction.fromString(request.getBy()), "title"));
    }

    private Specification<Lot> getSpecification(LotSearchRequest request, boolean deleted) {
        return Specification
                .where(LotSpecification.hasName(request.getName()))
                .and(LotSpecification.priceGreaterThanOrEqualTo(request.getPriceFrom()))
                .and(LotSpecification.priceLessThanOrEqualTo(request.getPriceTo()))
                .and(LotSpecification.isDeleted(deleted));
    }
}
