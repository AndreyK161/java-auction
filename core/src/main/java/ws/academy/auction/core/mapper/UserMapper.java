package ws.academy.auction.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ws.academy.auction.api.dto.rq.users.CreateUserRq;
import ws.academy.auction.api.dto.rq.users.RegisterUserRq;
import ws.academy.auction.api.dto.rq.users.UserSearchRequest;
import ws.academy.auction.api.dto.rs.ListPageData;
import ws.academy.auction.api.dto.rs.users.UserDetails;
import ws.academy.auction.api.dto.rs.users.UserListItem;
import ws.academy.auction.core.entity.User;

/**
 * Маппер для сущности User
 */
@Mapper
public interface UserMapper {
    /**
     * Получение сущности User
     *
     * @param request объект - запрос передаваемый клиентом
     * @return User объект - объект-сущность User
     */
    @Mapping(source = "fullName", target = "username")
    User buildUser(CreateUserRq request);

    /**
     * Получение сущности User
     *
     * @param request объект - запрос передаваемый клиентом
     * @return User объект - объект-сущность User
     */
    @Mapping(source = "fullName", target = "username")
    User buildRegisterUser(RegisterUserRq request);

    /**
     * Получение dto-объекта сущности User
     *
     * @param user объект - передаваемая сущность для маппинга в сущность
     * @return UserDetails объект - объект-dto сущности User
     */
    @Mapping(source = "username", target = "fullName")
    UserDetails buildUserDetails(User user);

    /**
     * Получение dto-объекта сущности для пагинации
     *
     * @param request объект - запрос передаваемый клиентом
     * @param size целое число - количество элементов на странице
     * @param userPage объект - страница с данными, возвращаемая Spring Data,
     *             содержащая коллекцию объектов Lot и информацию о пагинации
     * @return ListPageData объект - dto-объект сущности User, содержащий результаты с пагинацией
     */
    ListPageData buildListPageData(UserSearchRequest request, int size, Page<User> userPage);

    /**
     * Получение dto-объекта сущности Lot
     *
     * @param user объект - передаваемая сущность для маппинга в dto
     * @return UserListItem объект - dto-объект сущности User
     */
    @Mapping(source = "username", target = "fullName")
    UserListItem buildUserListItem(User user);
}
