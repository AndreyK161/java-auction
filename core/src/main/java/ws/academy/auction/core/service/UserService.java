package ws.academy.auction.core.service;

import org.springframework.stereotype.Service;
import ws.academy.auction.api.dto.rq.users.CreateUserRq;
import ws.academy.auction.api.dto.rq.users.UpdateUserRq;
import ws.academy.auction.api.dto.rq.users.UserSearchRequest;
import ws.academy.auction.api.dto.rs.GuidRs;
import ws.academy.auction.api.dto.rs.users.UserDetails;
import ws.academy.auction.api.dto.rs.users.UserList;

import java.util.UUID;

/**
 * Сервис по управлению пользователями
 */
@Service
public interface UserService {
    /**
     * Создание пользователя
     *
     * @param request объект - запрос передаваемый пользователем
     * @return GuidRs объект - объект-dto возвращаемая UUID сущности
     */
    GuidRs addUser(CreateUserRq request);

    /**
     * Получение пользователя по переданному идентификатору
     *
     * @param guid идентификатор сущности User
     * @return UserDetails объект - объект-dto сущности User
     */
    UserDetails findByGuid(UUID guid);

    /**
     * Получение списка пользователей с использованием фильтров
     *
     * @param request объект, содержащий фильтры
     * @return UserList - объект, список лотов с указанием переданных фильтров
     */
    UserList getUsers(UserSearchRequest request);

    /**
     * Обновление пользователя
     *
     * @param guid идентификатор пользователя
     * @param request объект, содержащий данные для обновления существующего пользователя
     */
    void updateUserByGuid(UUID guid, UpdateUserRq request);

    /**
     * Удаление пользователя
     *
     * @param guid идентификатор пользователя
     */
    void deleteUserByGuid(UUID guid);
}
