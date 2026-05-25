package ws.academy.auction.core.producer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ws.academy.auction.core.dto.BidStatusMessage;
import ws.academy.auction.core.producer.BidStatusProducer;

@Slf4j
@Component
@RequiredArgsConstructor
public class BidStatusRabbitProducer implements BidStatusProducer {

    private final RabbitTemplate bidRabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.status-routing-key}")
    private String bidStatusRoutingKey;

    @Override
    public void sendBidStatus(BidStatusMessage bidStatusMessage) {
        bidRabbitTemplate.convertAndSend(exchange, bidStatusRoutingKey, bidStatusMessage);
        log.info("Отправка BidStatusMessage в очередь ответа: {}", bidStatusMessage);
    }
}
