package ws.academy.auction.core.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ws.academy.auction.core.configuration.properties.BetRabbitMqProperties;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfiguration {
    private final BetRabbitMqProperties properties;

    @Bean
    public RabbitTemplate betRabbitTemplate(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setRoutingKey(properties.getRoutingKey());
        rabbitTemplate.setExchange(properties.getExchange());
        rabbitTemplate.setDefaultReceiveQueue(properties.getQueueName());

        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
