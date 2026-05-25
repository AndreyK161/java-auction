package ws.academy.auction.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ws.academy.auction.core.entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий по управлению пользователями в базе данных
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    /**
     * Получение Optional-сущности User
     *
     * @param email электронная почта пользователя
     * @return Optional<User> объект - Optional-сущность User
     */
    Optional<User> findByEmail(String email);

    /**
     * Получение Optional-сущности User
     *
     * @param guid идентификатор keycloak'а
     * @return Optional<User> объект - Optional-сущность User
     */
    Optional<User> findByGuidKeycloak(UUID guid);
}
