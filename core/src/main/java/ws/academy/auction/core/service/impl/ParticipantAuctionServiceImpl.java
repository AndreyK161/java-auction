package ws.academy.auction.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ws.academy.auction.api.dto.rq.lots.LotGuidRq;
import ws.academy.auction.api.dto.rq.participantAuctions.ParticipantAuctionSearchRq;
import ws.academy.auction.api.dto.rq.participants.ParticipantSearchRq;
import ws.academy.auction.api.dto.rq.participants.SubmittingRq;
import ws.academy.auction.api.dto.rs.GuidRs;
import ws.academy.auction.api.dto.rs.ListPageData;
import ws.academy.auction.api.dto.rs.lots.LotSummaryRs;
import ws.academy.auction.api.dto.rs.participantAuctions.ListParticipantAuctionRs;
import ws.academy.auction.api.dto.rs.participantAuctions.ParticipantAuctionItem;
import ws.academy.auction.api.dto.rs.participants.ParticipantAuctionDetails;
import ws.academy.auction.api.dto.rs.participants.ParticipantListRs;
import ws.academy.auction.api.dto.rs.participants.RegisteredParticipantRs;
import ws.academy.auction.api.dto.rs.users.UserDetails;
import ws.academy.auction.core.enrichers.AuctionLotEnricher;
import ws.academy.auction.core.entity.*;
import ws.academy.auction.core.enums.LotStatus;
import ws.academy.auction.core.enums.ParticipantAuctionStatus;
import ws.academy.auction.core.enums.ParticipantSortField;
import ws.academy.auction.core.exception.NotFoundException;
import ws.academy.auction.core.helpers.AuctionHelper;
import ws.academy.auction.core.helpers.AuctionLotHelper;
import ws.academy.auction.core.helpers.ParticipantHelper;
import ws.academy.auction.core.jpaspecifications.ParticipantAuctionSpecification;
import ws.academy.auction.core.mapper.ParticipantAuctionMapper;
import ws.academy.auction.core.repository.AuctionRepository;
import ws.academy.auction.core.repository.LotRepository;
import ws.academy.auction.core.repository.ParticipantAuctionRepository;
import ws.academy.auction.core.repository.ParticipantRepository;
import ws.academy.auction.core.service.CurrentUserService;
import ws.academy.auction.core.service.ParticipantAuctionService;
import ws.academy.auction.core.enrichers.impl.ParticipantAuctionEnricherImpl;
import ws.academy.auction.core.enrichers.impl.ParticipantEnricherImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ParticipantAuctionServiceImpl implements ParticipantAuctionService {
    private final AuctionHelper auctionHelper;
    private final ParticipantHelper participantHelper;
    private final AuctionLotHelper auctionLotHelper;
    private final AuctionRepository auctionRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantAuctionRepository participantAuctionRepository;
    private final LotRepository lotRepository;
    private final CurrentUserService currentUserService;
    private final ParticipantAuctionMapper participantAuctionMapper;
    private final ParticipantAuctionEnricherImpl participantAuctionEnricher;
    private final ParticipantEnricherImpl participantEnricher;
    private final AuctionLotEnricher auctionLotEnricher;

    @Override
    public GuidRs submittingAnAuction(UUID guid, SubmittingRq request) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);

        Participant participant = participantHelper.getParticipantOrThrow(request.getParticipant());

        ParticipantAuction participantAuction = createParticipantAuction(participant, auction);
        auction.getParticipantAuctionList().add(participantAuction);

        if (request.getLots() != null && !request.getLots().isEmpty()) {
            List<UUID> lotGuids = request.getLots().stream()
                    .map(LotGuidRq::getLot)
                    .collect(Collectors.toList());
            processLots(lotGuids, auction, participant);
        }

        auctionRepository.save(auction);
        participantAuctionRepository.save(participantAuction);
        return new GuidRs(auction.getGuid());
    }

    @Override
    public void submittingAnAuction(UUID guid) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        Participant participant = getCurrentParticipant();

        ParticipantAuction participantAuction = createParticipantAuction(participant, auction);
        participantAuctionRepository.save(participantAuction);
    }

    @Override
    public void actionSubmittingAnAuction(UUID guid, String action, UUID guidParticipant) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        Participant participant = participantHelper.getParticipantOrThrow(guidParticipant);
        ParticipantAuction participantAuction = getParticipantAuctionOrThrow(participant, auction);

        participantAuction.setStatus(ParticipantAuctionStatus.from(action));
        participantAuctionRepository.save(participantAuction);
    }

    @Override
    public ListParticipantAuctionRs getParticipantAuctions(UUID guid, ParticipantAuctionSearchRq request) {
        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getCount() != null ? request.getCount() : 30;

        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        Pageable pageable = PageRequest.of(page, size);

        Specification<ParticipantAuction> spec = getSpecificationWithStatusAndLots(auction);

        Page<ParticipantAuction> participantAuctionPage = participantAuctionRepository.findAll(spec, pageable);

        List<ParticipantAuctionItem> participantAuctionItems = getParticipantAuctionItems(participantAuctionPage);

        ListPageData pageData = getPageData(size, page, participantAuctionItems.size());

        return new ListParticipantAuctionRs(participantAuctionItems, pageData);
    }

    @Override
    public ParticipantListRs getParticipants(UUID auctionGuid, ParticipantSearchRq request) {
        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getCount() != null ? request.getCount() : 30;

        Auction auction = auctionHelper.getAuctionOrThrow(auctionGuid);
        ParticipantSortField sortField = ParticipantSortField.from(request.getSort());
        Sort.Direction direction = "desc".equalsIgnoreCase(request.getSort()) ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField.getSortField()));

        Specification<ParticipantAuction> spec = getSpecificationBasic(auction);

        Page<ParticipantAuction> resultPage = participantAuctionRepository.findAll(spec, pageable);

        List<RegisteredParticipantRs> participantDtoList = resultPage.getContent().stream()
                .map(ParticipantAuction::getParticipant)
                .distinct()
                .map(participantEnricher::toRegisteredParticipant)
                .toList();

        ListPageData pageData = getPageData(size, page, participantDtoList.size());

        return ParticipantListRs.builder()
                .participants(participantDtoList)
                .page(pageData)
                .build();
    }

    @Override
    public ParticipantAuctionDetails getParticipantAuction(UUID guid, UUID participantGuid) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        Participant participant = participantHelper.getParticipantOrThrow(participantGuid);

        ParticipantAuction participantAuction = getParticipantAuctionOrThrow(participant, auction);

        ParticipantAuctionDetails participantAuctionDetails = participantAuctionMapper.toDetails(participantAuction);
        List<Lot> forSaleLots = lotRepository.findAllByOwnerAndLotStatus(participant, LotStatus.ON_TRADE);

        List<LotSummaryRs> forSaleLotsDto = participantEnricher.getForSaleLots(forSaleLots);

        participantAuctionDetails.setForSaleLots(forSaleLotsDto);
        return participantAuctionDetails;
    }

    @Override
    public void deleteParticipantAuction(UUID guid, UUID participantGuid) {
        Auction auction = auctionHelper.getAuctionOrThrow(guid);
        Participant participant = participantHelper.getParticipantOrThrow(participantGuid);

        ParticipantAuction participantAuction = getParticipantAuctionOrThrow(participant, auction);

        participantAuctionRepository.delete(participantAuction);
    }

    private ParticipantAuction getParticipantAuctionOrThrow(Participant participant, Auction auction) {
        return participantAuctionRepository.findAuctionParticipantByAuctionAndParticipant(auction, participant)
                .orElseThrow(() -> new NotFoundException("Участник аукциона не найден"));
    }

    private ParticipantAuction createParticipantAuction(Participant participant, Auction auction) {
        ParticipantAuction pa = new ParticipantAuction();
        pa.setAuction(auction);
        pa.setParticipant(participant);
        pa.setParticipantNumber(
                Optional.ofNullable(participantAuctionRepository.findMaxNumber(auction)).orElse(0) + 1);
        pa.setStatus(ParticipantAuctionStatus.NEW);
        pa.setCreateAt(LocalDateTime.now());
        return pa;
    }

    private void processLots(List<UUID> lotGuids, Auction auction, Participant participant) {
        List<Lot> lots = lotRepository.findAllByGuidIn(lotGuids);
        lots.forEach(lot -> {
            auctionLotEnricher.buildAuctionLot(auction, lot);
            lot.setOwner(participant);
            lot.setLotStatus(LotStatus.AUCTION_REQUEST);
        });

        lotRepository.saveAll(lots);
    }

    private Participant getCurrentParticipant() {
        UserDetails userDetails = currentUserService.showCurrentUser();
        return participantRepository.findByUserGuid(userDetails.getGuid());
    }

    private List<ParticipantAuctionItem> getParticipantAuctionItems(Page<ParticipantAuction> participantAuctionPage) {
        return participantAuctionPage.stream()
                .map(pa -> {
                    List<LotGuidRq> lots = getLotGuidRqList(pa.getAuction(), pa.getParticipant());
                    return getParticipantAuctionItem(pa, lots);
                })
                .toList();
    }

    private ParticipantAuctionItem getParticipantAuctionItem(
            ParticipantAuction participantAuction, List<LotGuidRq> lots) {
        participantAuctionEnricher.buildAuctionSubmittingRs(participantAuction.getAuction());
        return participantAuctionEnricher.buildParticipantAuctionItem(participantAuction, lots);
    }

    private List<LotGuidRq> getLotGuidRqList(Auction auction, Participant participant) {
        return participant.getLotsOwner().stream()
                .map(lot -> {
                    auctionLotHelper.getAuctionLotOrThrow(auction, lot);
                    return new LotGuidRq(lot.getGuid());
                })
                .toList();
    }

    private Specification<ParticipantAuction> getSpecificationBasic(Auction auction) {
        return ParticipantAuctionSpecification.byAuction(auction);
    }

    private Specification<ParticipantAuction> getSpecificationWithStatusAndLots(Auction auction) {
        return Specification
                .where(ParticipantAuctionSpecification.byAuction(auction))
                .and(ParticipantAuctionSpecification.byStatus(ParticipantAuctionStatus.NEW));
    }

    private ListPageData getPageData(int size, int page, int items) {
        return ListPageData.builder()
                .totalPages(size)
                .page(page + 1)
                .size(items)
                .build();
    }
}
