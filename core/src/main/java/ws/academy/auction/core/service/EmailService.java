package ws.academy.auction.core.service;

import ws.academy.auction.api.dto.UserEmailData;

/**
 * Сервис по отправке email-сообщений
 */
public interface EmailService {

    /**
     * Отправка email-сообщения
     *
     * @param data dto-объект хранящий данные пользователя
     */
    void sendEmailMessage(UserEmailData data);
}
