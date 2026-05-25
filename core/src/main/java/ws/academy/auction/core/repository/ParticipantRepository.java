package ws.academy.auction.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ws.academy.auction.core.entity.Participant;
import ws.academy.auction.core.entity.User;

import java.util.UUID;

/**
 * Репозиторий по управлению участниками лотов в базе данных
 */
@Repository
public interface ParticipantRepository extends JpaRepository<Participant, UUID>, JpaSpecificationExecutor<Participant> {
    /**
     * Проверка существования пользователя
     *
     * @param user сущность User
     * @return true - пользователь существует; false - пользователь не существует
     */
    boolean existsByUser(User user);

    /**
     * Удаление участника являющегося пользователем
     *
     * @param user сущность User
     */
    void deleteByUser(User user);

    /**
     * Получение участника по идентификатору пользователя
     *
     * @param guid идентифатор пользователя
     * @return Participant объект - сущность Participant
     */
    Participant findByUserGuid(UUID guid);
}
