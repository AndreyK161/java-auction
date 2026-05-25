package ws.academy.auction.core.producer;

import ws.academy.auction.core.dto.BidStatusMessage;

/**
 * Интерфейс публикации сообщения с данными о статусе ставки
 */
public interface BidStatusProducer {
    /**
     * Отправка сообщения с данными о статусе ставки
     */
    void sendBidStatus(BidStatusMessage bidStatusMessage);
}
