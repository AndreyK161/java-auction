package ws.academy.auction.core.helpers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.academy.auction.core.entity.Auction;
import ws.academy.auction.core.entity.Participant;
import ws.academy.auction.core.entity.ParticipantAuction;
import ws.academy.auction.core.exception.NotFoundException;
import ws.academy.auction.core.helpers.ParticipantAuctionHelper;
import ws.academy.auction.core.repository.ParticipantAuctionRepository;

@Component
@RequiredArgsConstructor
public class ParticipantAuctionHelperImpl implements ParticipantAuctionHelper {
    private final ParticipantAuctionRepository participantAuctionRepository;

    @Override
    public ParticipantAuction getParticipantAuctionOrThrow(Auction auction, Participant participant) {
        return participantAuctionRepository.findAuctionParticipantByAuctionAndParticipant(auction, participant)
                .orElseThrow(() -> new NotFoundException("Участник не участвует в торгах"));
    }
}
