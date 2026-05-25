package ws.academy.auction.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ws.academy.auction.core.entity.PasswordRecovery;
import ws.academy.auction.core.entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий сущности PasswordRecovery
 */
@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, UUID> {
    /**
     * Возвращает запись восстановления пароля для указанного пользователя.
     *
     * @param user Пользователь, для которого ищется запись восстановления
     * @param recoverySecret Секретный ключ, передаваемый в письме пользователю
     * @return Запись восстановления пароля (если есть)
     */
    Optional<PasswordRecovery> findByUserGuidAndRecoverySecret(User user, String recoverySecret);
}
