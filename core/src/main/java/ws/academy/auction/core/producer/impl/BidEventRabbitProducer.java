package ws.academy.auction.core.producer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ws.academy.auction.core.dto.BidMessage;
import ws.academy.auction.core.producer.BidEventProducer;

@Slf4j
@Component
@RequiredArgsConstructor
public class BidEventRabbitProducer implements BidEventProducer {

    private final RabbitTemplate bidRabbitTemplate;

    public void sendBidEvent(BidMessage bidMessage) {
        bidRabbitTemplate.convertAndSend(bidMessage);
        log.info("Событие успешно опубликовано: {}", bidMessage);
    }
}
