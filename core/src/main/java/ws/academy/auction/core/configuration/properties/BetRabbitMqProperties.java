package ws.academy.auction.core.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки очередей для событий по ставкам
 */
@Data
@ConfigurationProperties(prefix = "rabbitmq")
public class BetRabbitMqProperties {
    /**
     * Наименование очереди
     */
    private String queueName;
    /**
     * Наименование ключа маршрутизации
     */
    private String routingKey;
    /**
     * Наименование обменника
     */
    private String exchange;
}
