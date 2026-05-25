package ws.academy.auction.core.consumer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;
import ws.academy.auction.core.dto.BidStatusMessage;
import ws.academy.auction.core.consumer.BidStatusConsumer;
import ws.academy.auction.core.messages.BidResultGateway;

@Slf4j
@RabbitListener(
        containerFactory = "rabbitListenerContainerFactory",
        bindings = @QueueBinding(
                value = @Queue(value = "${rabbitmq.bid-status-queue-name}", durable = "false"),
                key = "${rabbitmq.status-routing-key}",
                exchange = @Exchange(value = "${rabbitmq.exchange}", type = "topic")
        ),
        messageConverter = "jsonMessageConverter"
)
@Component
@RequiredArgsConstructor
public class BidStatusRabbitConsumer implements BidStatusConsumer {
    private final BidResultGateway bidResultGateway;

    @Override
    @RabbitHandler
    public void handleBidMessage(BidStatusMessage bidStatusMessage) {
        bidResultGateway.complete(bidStatusMessage.getGuid(), bidStatusMessage);
        log.info("Получен статус обработанной ставки: {}", bidStatusMessage);
    }
}
