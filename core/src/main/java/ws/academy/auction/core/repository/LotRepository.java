package ws.academy.auction.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ws.academy.auction.core.entity.Lot;
import ws.academy.auction.core.entity.Participant;
import ws.academy.auction.core.enums.LotStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий по управлению лотами в базе данных
 */

@Repository
public interface LotRepository extends JpaRepository<Lot, UUID>, JpaSpecificationExecutor<Lot> {

    /**
     * Получение Optional-сущности Lot
     *
     * @param guid идентификатор лота
     * @return Optional<Lot> объект - Optional-сущность Lot
     */
    Optional<Lot> findByGuid(UUID guid);

    /**
     * Получение списка сущностей Lot в передаваемом списке идентификаторов
     *
     * @param lotGuids список идентификаторов лотов
     * @return Список сущностей Lot
     */
    List<Lot> findAllByGuidIn(List<UUID> lotGuids);

    /**
     * Получение списка сущностей Lot с указанными продавцами
     *
     * @param owner участник-продавец
     * @param status cтатус лота для фильтрации
     * @return Список сущностей Lot
     */
    List<Lot> findAllByOwnerAndLotStatus(Participant owner, LotStatus status);

    List<Lot> findAllByOwnerAndLotStatusIn(Participant owner, List<LotStatus> statuses);

    /**
     * Получение списка сущностей Lot с указанными покупателями
     *
     * @param buyer участник-покупатель
     * @param status cтатус лота для фильтрации
     * @return Список сущностей Lot
     */
    List<Lot> findAllByBuyerAndLotStatus(Participant buyer, LotStatus status);
}
