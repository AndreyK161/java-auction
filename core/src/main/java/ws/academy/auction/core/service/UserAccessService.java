package ws.academy.auction.core.service;

import ws.academy.auction.api.dto.rq.users.*;
import ws.academy.auction.api.dto.rs.users.ResultMessageRs;
import ws.academy.auction.api.dto.rs.users.JwtRs;

/**
 * Сервис по управлению доступа пользователя в систему
 */
public interface UserAccessService {
    /**
     * Аутентификация пользователя
     *
     * @param request объект - запрос передаваемый пользователем
     * @return JwtRs объект - dto-объект хранящий в себе accessToken и refreshToken
     */
    JwtRs loginUser(AuthenticateUserRq request);

    /**
     * Обновление токена пользователя
     *
     * @param request объект - запрос передаваемый пользователем
     * @return JwtRs объект - dto-объект хранящий в себе accessToken и refreshToken
     */
    JwtRs refreshTokenUser(RefreshTokenRq request);

    /**
     * Регистрация пользователя
     *
     * @param request объект - запрос передаваемый пользователем
     * @return String объект - ссылка отправляемая письмом на email пользователя
     */
    ResultMessageRs registerUser(RegisterUserRq request);

    /**
     * Активация аккаунта пользователя
     *
     * @param request объект - запрос передаваемый пользователем
     * @return String сообщение об успешной активации аккаунта
     */
    ResultMessageRs activateUser(ActivateUserRq request);
}
