package ws.academy.auction.core.producer;

import ws.academy.auction.core.dto.BidMessage;

/**
 * Интерфейс публикации события с данными ставки
 */
public interface BidEventProducer {
    /**
     * Отправка события с данными ставки
     */
    void sendBidEvent(BidMessage bidMessage);
}
