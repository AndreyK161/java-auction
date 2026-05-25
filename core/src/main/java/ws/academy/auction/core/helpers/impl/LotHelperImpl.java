package ws.academy.auction.core.helpers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.academy.auction.core.entity.Lot;
import ws.academy.auction.core.exception.NotFoundException;
import ws.academy.auction.core.helpers.LotHelper;
import ws.academy.auction.core.repository.LotRepository;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LotHelperImpl implements LotHelper {
    private final LotRepository lotRepository;

    @Override
    public Lot getLotOrThrow(UUID guid) {
        return lotRepository.findById(guid).orElseThrow(() -> new NotFoundException("Лот не найден: id = " + guid));
    }
}
