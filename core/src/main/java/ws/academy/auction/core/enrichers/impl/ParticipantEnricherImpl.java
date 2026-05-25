package ws.academy.auction.core.enrichers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.academy.auction.api.dto.rs.lots.LotSummaryRs;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;
import ws.academy.auction.api.dto.rs.participants.RegisteredParticipantRs;
import ws.academy.auction.core.enrichers.ParticipantEnricher;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Lot;
import ws.academy.auction.core.entity.Participant;
import ws.academy.auction.core.enums.LotStatus;
import ws.academy.auction.core.helpers.AuctionLotHelper;
import ws.academy.auction.core.mapper.ParticipantMapper;
import ws.academy.auction.core.repository.LotRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ParticipantEnricherImpl implements ParticipantEnricher {
    private final AuctionLotHelper auctionLotHelper;
    private final LotRepository lotRepository;
    private final LotEnricherImpl lotEnricher;
    private final ParticipantMapper participantMapper;

    @Override
    public RegisteredParticipantRs toRegisteredParticipant(Participant participant) {
        List<Lot> forSaleLots = lotRepository.findAllByOwnerAndLotStatus(participant, LotStatus.ON_TRADE);
        List<Lot> purchaseLots = lotRepository.findAllByBuyerAndLotStatus(participant, LotStatus.SOLD);

        List<LotSummaryRs> forSaleLotsDto = getForSaleLots(forSaleLots);
        List<LotSummaryRs> purchaseLotsDto = getPurchaseLots(purchaseLots);

        return RegisteredParticipantRs.builder()
                .id(participant.getGuid())
                .email(participant.getEmail())
                .fullName(participant.getFullName())
                .forSaleLots(forSaleLotsDto)
                .purchaseLots(purchaseLotsDto)
                .actions(List.of("DELETE"))
                .build();
    }

    @Override
    public List<LotSummaryRs> getPurchaseLots(List<Lot> purchaseLots) {
        return purchaseLots.stream()
                .map(lot -> {
                    AuctionLot auctionLot = auctionLotHelper.getAuctionLotOrThrowWithLot(lot);
                    return lotEnricher.toLotSummary(auctionLot);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LotSummaryRs> getForSaleLots(List<Lot> saleLots) {
        return saleLots.stream()
                .map(lot -> {
                    AuctionLot auctionLot = auctionLotHelper.getAuctionLotOrThrowWithLot(lot);
                    return lotEnricher.toLotSummary(auctionLot);
                })
                .collect(Collectors.toList());

    }

    @Override
    public ParticipantDetails toParticipantDto(Participant participant) {
        return participantMapper.buildParticipantDetails(participant);
    }
}
