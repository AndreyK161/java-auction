package ws.academy.auction.core.consumer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ws.academy.auction.core.dto.BidMessage;
import ws.academy.auction.core.dto.BidStatusMessage;
import ws.academy.auction.core.entity.Bid;
import ws.academy.auction.core.consumer.BidEventConsumer;
import ws.academy.auction.core.producer.BidStatusProducer;
import ws.academy.auction.core.service.BidEventWorkerService;

@Slf4j
@RabbitListener(
        containerFactory = "rabbitListenerContainerFactory",
        bindings = @QueueBinding(
                value = @Queue(value = "${rabbitmq.bid-process-queue-name}", durable = "false"),
                key = "${rabbitmq.routing-key}",
                exchange = @Exchange(value = "${rabbitmq.exchange}", type = "topic")
        ),
        messageConverter = "jsonMessageConverter"
)
@Component
@RequiredArgsConstructor
public class BidEventRabbitConsumer implements BidEventConsumer {
    private final BidStatusProducer bidStatusProducer;
    private final BidEventWorkerService bidWorkerService;

    @Override
    @RabbitHandler
    public void handleBidEvent(@Payload BidMessage bidMessage) {
        Bid bid = bidWorkerService.workBidEvent(bidMessage);
        bidStatusProducer.sendBidStatus(new BidStatusMessage(bid.getBidStatus().name(), bid.getGuid()));
        log.info("Событие успешно обработано: {}", bidMessage);
    }
}
