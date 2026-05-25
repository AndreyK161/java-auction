package ws.academy.auction.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ws.academy.auction.core.entity.Photo;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий по управлению изображениями в базе данных
 */
@Repository
public interface PhotoRepository extends JpaRepository<Photo, UUID> {
    /**
     * Поиск всех фотографий по типу и идентификатору сущности
     *
     * @param entityType тип сущности
     * @param guid идентификатор сущности
     * @return Список сущности Photo
     */
    List<Photo> findAllByEntityTypeAndEntityGuid(String entityType, UUID guid);

    /**
     * Удаление всех фотографий по типу и идентификатору сущности
     *
     * @param entityType тип сущности
     * @param guid идентификатор сущности
     */
    void deleteAllByEntityTypeAndGuid(String entityType, UUID guid);
}
