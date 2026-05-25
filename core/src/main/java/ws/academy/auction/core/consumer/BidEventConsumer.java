package ws.academy.auction.core.consumer;

import ws.academy.auction.core.dto.BidMessage;

/**
 * Интерфейс прослушивания событий по ставкам
 */
public interface BidEventConsumer {
    /**
     * Обработка события по ставке
     */
    void handleBidEvent(BidMessage bidMessage);
}
