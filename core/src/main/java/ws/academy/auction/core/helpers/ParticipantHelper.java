package ws.academy.auction.core.helpers;

import ws.academy.auction.core.entity.Participant;

import java.util.UUID;

public interface ParticipantHelper {
    Participant getParticipantOrThrow(UUID guid);
}
