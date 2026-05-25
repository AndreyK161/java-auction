package ws.academy.auction.core.helpers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.academy.auction.core.entity.Participant;
import ws.academy.auction.core.exception.NotFoundException;
import ws.academy.auction.core.repository.ParticipantRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ParticipantHelperImpl implements ws.academy.auction.core.helpers.ParticipantHelper {
    private final ParticipantRepository participantRepository;

    @Override
    public Participant getParticipantOrThrow(UUID guid) {
        return participantRepository.findById(guid)
                .orElseThrow(() -> new NotFoundException("Участник не найден: id = " + guid));
    }
}
