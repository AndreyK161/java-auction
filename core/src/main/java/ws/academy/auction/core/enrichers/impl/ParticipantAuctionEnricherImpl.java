package ws.academy.auction.core.enrichers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.academy.auction.api.dto.rq.lots.LotGuidRq;
import ws.academy.auction.api.dto.rs.auctions.AuctionRs;
import ws.academy.auction.api.dto.rs.auctions.AuctionSubmittingRs;
import ws.academy.auction.api.dto.rs.participantAuctions.ParticipantAuctionItem;
import ws.academy.auction.api.dto.rs.participants.RegisteredParticipantRs;
import ws.academy.auction.core.enrichers.ParticipantAuctionEnricher;
import ws.academy.auction.core.entity.*;
import ws.academy.auction.core.mapper.AuctionMapper;
import ws.academy.auction.core.mapper.ParticipantMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ParticipantAuctionEnricherImpl implements ParticipantAuctionEnricher {
    private final AuctionMapper auctionMapper;
    private final ParticipantMapper participantMapper;
    private final ParticipantEnricherImpl participantEnricher;

    @Override
    public AuctionSubmittingRs buildAuctionSubmittingRs(Auction auction) {
        AuctionSubmittingRs dto = auctionMapper.buildAuctionSubmittingRs(auction);
        dto.setStartDate(auction.getStartDate());
        return dto;
    }

    @Override
    public ParticipantAuctionItem buildParticipantAuctionItem(ParticipantAuction pa, List<LotGuidRq> lots) {
        return new ParticipantAuctionItem(
                pa.getCreateAt(),
                participantMapper.buildParticipantDetails(pa.getParticipant()),
                buildAuctionSubmittingRs(pa.getAuction()),
                lots,
                List.of("ACCEPT"));
    }

    @Override
    public void setAuctionParticipantList(AuctionRs auctionRs, Auction auction) {
        List<ParticipantAuction> participantEntities = auction.getParticipantAuctionList();

        List<RegisteredParticipantRs> participantDto = participantEntities.stream()
                .map(participantAuction -> {
                    Participant participant = participantAuction.getParticipant();

                    return participantEnricher.toRegisteredParticipant(participant);
                })
                .collect(Collectors.toList());

        auctionRs.setParticipants(participantDto);
    }

}
