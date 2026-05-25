package ws.academy.auction.core.service;

import org.springframework.stereotype.Service;
import ws.academy.auction.api.dto.rq.users.EmailRq;
import ws.academy.auction.api.dto.rq.users.ResetPasswordRq;
import ws.academy.auction.api.dto.rs.users.ResultMessageRs;

@Service
public interface PasswordRecoveryService {
    /**
     * Отправляет ссылку для восстановления пароля на email пользователя
     *
     * @param request объект - запрос передаваемый пользователем
     * @return String объект - ссылка отправляемая письмом на email пользователя
     */
    ResultMessageRs sendPasswordRecoveryLink(EmailRq request);

    /**
     * Сбрасывает пароль пользователя по полученному коду (хэшу)
     *
     * @param request объект - запрос передаваемый пользователем
     * @return String сообщение об успешной операции смены пароля
     */
    ResultMessageRs resetPassword(ResetPasswordRq request);
}
