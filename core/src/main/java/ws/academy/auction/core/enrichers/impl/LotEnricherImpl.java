package ws.academy.auction.core.enrichers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.academy.auction.api.dto.rs.auctions.AuctionRs;
import ws.academy.auction.api.dto.rs.auctions.AuctionShortRs;
import ws.academy.auction.api.dto.rs.lots.LotDetails;
import ws.academy.auction.api.dto.rs.lots.LotSummaryRs;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;
import ws.academy.auction.core.enrichers.LotEnricher;
import ws.academy.auction.core.entity.Auction;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Lot;
import ws.academy.auction.core.helpers.PhotoHelper;
import ws.academy.auction.core.mapper.LotMapper;
import ws.academy.auction.core.mapper.PhotoMapper;
import ws.academy.auction.core.repository.PhotoRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LotEnricherImpl implements LotEnricher {
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final LotMapper lotMapper;
    private final PhotoHelper photoHelper;

    @Override
    public LotDetails bindLotWithDetails(Lot lot, ParticipantDetails participant, AuctionRs auctionRs) {
        LotDetails lotDetails = lotMapper.buildLotDetailsRs(lot);
        lotDetails.setOwner(participant);
        lotDetails.setPhotos(photoHelper.getPhotoDetailsList(lot));

        if (auctionRs != null) {
            lotDetails.setCurrentAuction(auctionRs);
        }

        return lotDetails;
    }

    @Override
    public LotSummaryRs toLotSummary(AuctionLot auctionLot) {
        Lot lot = auctionLot.getLot();
        Auction auction = auctionLot.getAuction();
        return LotSummaryRs.builder()
                .guid(lot.getGuid())
                .title(lot.getTitle())
                .startPrice(lot.getStartPrice())
                .createdAt(lot.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")))
                .lastAuction(auction != null
                        ? AuctionShortRs.builder()
                        .guid(auction.getGuid())
                        .build()
                        : null)
                .photo(photoRepository.findAllByEntityTypeAndEntityGuid("LOT", lot.getGuid())
                        .stream()
                        .map(photoMapper::buildPhotoDetails)
                        .collect(Collectors.toList()))
                .actions(List.of("DELETE"))
                .build();
    }
}
