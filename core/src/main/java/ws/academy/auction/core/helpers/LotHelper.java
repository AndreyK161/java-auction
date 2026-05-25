package ws.academy.auction.core.helpers;

import ws.academy.auction.core.entity.Lot;

import java.util.UUID;

public interface LotHelper {
    Lot getLotOrThrow(UUID guid);
}
