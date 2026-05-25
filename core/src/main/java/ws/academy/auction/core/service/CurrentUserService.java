package ws.academy.auction.core.service;

import ws.academy.auction.api.dto.rq.users.FullNameRq;
import ws.academy.auction.api.dto.rs.users.UserDetails;

/**
 * Сервис по управлению текущих авторизованных пользователей
 */
public interface CurrentUserService {

    /**
     * Получение текущего авторизованного пользователя
     *
     * @return UserDetails объект - dto-объект сущности User
     */
    UserDetails showCurrentUser();

    /**
     * Обновление текущего авторизованного пользователя
     *
     * @param request объект - dto-объект имени участника
     */
    void updateCurrentUser(FullNameRq request);
}
