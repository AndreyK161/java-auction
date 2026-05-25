package ws.academy.auction.core.enrichers;

import ws.academy.auction.api.dto.rs.lots.LotSummaryRs;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;
import ws.academy.auction.api.dto.rs.participants.RegisteredParticipantRs;
import ws.academy.auction.core.entity.Lot;
import ws.academy.auction.core.entity.Participant;

import java.util.List;

public interface ParticipantEnricher {

    RegisteredParticipantRs toRegisteredParticipant(Participant participant);

    List<LotSummaryRs> getPurchaseLots(List<Lot> purchaseLots);

    List<LotSummaryRs> getForSaleLots(List<Lot> saleLots);

    ParticipantDetails toParticipantDto(Participant participant);
}
