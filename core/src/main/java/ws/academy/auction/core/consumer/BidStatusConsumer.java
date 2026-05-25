package ws.academy.auction.core.consumer;

import ws.academy.auction.core.dto.BidStatusMessage;

/**
 * Интерфейс прослушивания сообщений о статусе ставки
 */
public interface BidStatusConsumer {
    /**
     * Обработка сообщения по статусу ставки
     */
    void handleBidMessage(BidStatusMessage bidStatusMessage);
}
