package ws.academy.auction.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ws.academy.auction.api.dto.rq.lots.PurchaseLotsSearchRq;
import ws.academy.auction.api.dto.rq.participants.CreateOperationAddAmountWalletRq;
import ws.academy.auction.api.dto.rq.participants.CreateOperationWalletRq;
import ws.academy.auction.api.dto.rq.participants.CreateOperationWithdrawAmountRq;
import ws.academy.auction.api.dto.rs.ListPageData;
import ws.academy.auction.api.dto.rs.lots.PurchaseInfo;
import ws.academy.auction.api.dto.rs.lots.PurchaseLotItem;
import ws.academy.auction.api.dto.rs.lots.PurchaseLotsList;
import ws.academy.auction.api.dto.rs.participants.OperationAccountWalletRs;
import ws.academy.auction.api.dto.rs.photos.PhotoDetails;
import ws.academy.auction.core.entity.*;
import ws.academy.auction.core.enums.LotStatus;
import ws.academy.auction.core.enums.TransactionType;
import ws.academy.auction.core.helpers.AuctionLotHelper;
import ws.academy.auction.core.helpers.BidHelper;
import ws.academy.auction.core.helpers.ParticipantHelper;
import ws.academy.auction.core.helpers.PhotoHelper;
import ws.academy.auction.core.jpaspecifications.LotSpecification;
import ws.academy.auction.core.mapper.LotMapper;
import ws.academy.auction.core.mapper.ParticipantMapper;
import ws.academy.auction.core.repository.*;
import ws.academy.auction.core.service.ParticipantService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantHelper participantHelper;
    private final BidHelper bidHelper;
    private final AuctionLotHelper auctionLotHelper;
    private final PhotoHelper photoHelper;
    private final ParticipantRepository participantRepository;
    private final TransactionRepository transactionRepository;
    private final LotRepository lotRepository;
    private final ParticipantMapper participantMapper;
    private final LotMapper lotMapper;

    @Override
    public OperationAccountWalletRs addAmountWallet(UUID guid, CreateOperationWalletRq request) {
        Participant participant = participantHelper.getParticipantOrThrow(guid);

        initTransaction(participant, getTransaction(request.getAmount(), request.getComment()));

        BigDecimal amountParticipant = participant.getBalance().add(request.getAmount());
        participant.setBalance(amountParticipant);
        participantRepository.save(participant);

        return participantMapper.buildOperationWallet(participant);
    }

    @Override
    public OperationAccountWalletRs getWallet(UUID guid) {
        Participant participant = participantHelper.getParticipantOrThrow(guid);
        return participantMapper.buildOperationWallet(participant);
    }

    @Override
    public void putAmountWallet(UUID guid, CreateOperationAddAmountWalletRq request) {
        Participant participant = participantHelper.getParticipantOrThrow(guid);

        BigDecimal amountParticipant = participant.getBalance().add(request.getAmount());
        participant.setBalance(amountParticipant);

        initTransaction(participant, getTransaction(request.getAmount(), "Пополнение личного счета"));
        participantRepository.save(participant);
    }

    @Override
    public void withdrawAmount(UUID guid, CreateOperationWithdrawAmountRq request) {
        Participant participant = participantHelper.getParticipantOrThrow(guid);

        BigDecimal amountParticipant = participant.getBalance().subtract(request.getAmount());
        participant.setBalance(amountParticipant);

        initTransaction(participant, Transaction.builder()
                .at(LocalDateTime.now())
                .amount(request.getAmount())
                .comment("Списание с личного счета")
                .type(TransactionType.WITHDRAW)
                .build());
        participantRepository.save(participant);
    }

    @Override
    public PurchaseLotsList getPurchaseLots(UUID guid, PurchaseLotsSearchRq request) {
        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getCount() != null ? request.getCount() : 30;

        Participant buyer = participantHelper.getParticipantOrThrow(guid);

        Pageable pageable = getPageable(page, size);

        Specification<Lot> spec = getSpecification(buyer, request.getParticipants());

        Page<Lot> lotPage = lotRepository.findAll(spec, pageable);

        List<PurchaseLotItem> purchaseLotItems = createPurchaseLotItem(lotPage);

        ListPageData listPageData = getPageData(size, page, purchaseLotItems.size());

        return createPurchaseLotList(purchaseLotItems, listPageData);
    }

    private Pageable getPageable(int page, int size) {
        return PageRequest.of(page, size);
    }

    private Specification<Lot> getSpecification(Participant buyer, List<UUID> owners) {
        return Specification
                .where(LotSpecification.hasBuyerLot(buyer))
                .and(LotSpecification.hasStatus(LotStatus.SOLD))
                .and(LotSpecification.hasOwnerGuids(owners));
    }

    private List<PurchaseLotItem> createPurchaseLotItem(Page<Lot> lotPage) {
        return lotPage.getContent().stream()
                .map(this::getPurchaseLotItem)
                .collect(Collectors.toList());
    }

    private PurchaseLotItem getPurchaseLotItem(Lot lot) {
        PurchaseLotItem item = lotMapper.buildPurchaseLotItem(lot);
        List<PhotoDetails> photos = photoHelper.getPhotoDetailsList(lot);
        AuctionLot auctionLot = auctionLotHelper.getArchiveAuctionLotFor(lot);
        Bid bid = bidHelper.getBidForMaxBuyerNumber(auctionLot);

        PurchaseInfo purchaseInfo = getPurchaseInfo(bid);

        item.setPurchaseInfo(purchaseInfo);
        item.setPhoto(photos);
        return item;
    }

    private PurchaseInfo getPurchaseInfo(Bid bid) {
        return PurchaseInfo.builder()
                .date(bid.getEndAt())
                .cost(bid.getAmount())
                .participant(participantMapper.buildParticipantDetails(bid.getBuyer()))
                .build();
    }

    private PurchaseLotsList createPurchaseLotList(List<PurchaseLotItem> purchaseLotItems, ListPageData listPageData) {
        return PurchaseLotsList.builder()
                .items(purchaseLotItems)
                .page(listPageData)
                .build();
    }

    private ListPageData getPageData(int size, int page, int items) {
        return ListPageData.builder()
                .totalPages(size)
                .page(page + 1)
                .size(items)
                .build();
    }

    private Transaction getTransaction(BigDecimal amount, String comment) {
        return Transaction.builder()
                .at(LocalDateTime.now())
                .amount(amount)
                .comment(comment)
                .type(TransactionType.DEPOSIT)
                .build();
    }

    private void initTransaction(Participant participant, Transaction transaction) {
        transaction.setParticipant(participant);
        transactionRepository.save(transaction);
    }
}
