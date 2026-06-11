package ws.academy.auction.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ws.academy.auction.core.entity.Auction;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Lot;
import ws.academy.auction.core.enums.LotStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuctionLotRepository extends JpaRepository<AuctionLot, UUID>, JpaSpecificationExecutor<AuctionLot> {

    /**
     * Получение Optional сущности при помощи сущностей: аукцион, лот
     *
     * @param auction сущность аукцион
     * @param lot сущность лот
     * @return Optional-объект сущности AuctionLot
     */
    Optional<AuctionLot> findByAuctionAndLot(Auction auction, Lot lot);

    /**
     * Получение Optional сущности при помощи сущности лот и ее статусу
     *
     * @param lot сущность лот
     * @param lotStatus статус лота
     * @return Optional-объект сущности AuctionLot
     */
    @SuppressWarnings("checkstyle:findByLotAndLot_LotStatus")
    Optional<AuctionLot> findByLotAndLot_LotStatus(Lot lot, LotStatus lotStatus);

    /**
     * Получение количества лотов в передаваемом идентификаторе аукциона
     *
     * @param auctionGuid идентификатор аукциона
     * @return Количество лотов в аукционе
     */
    @Query("select count(al) from AuctionLot al where al.auction.guid = :auctionGuid")
    long countByAuctionGuid(@Param("auctionGuid") UUID auctionGuid);

    /**
     * Получение суммы бюджета в передаваемом идентификаторе аукциона
     *
     * @param auctionGuid идентификатор аукциона
     * @return Сумма бюджета складываемая из начальной суммы лотов
     */
    @Query("select sum(al.lot.startPrice) from AuctionLot al where al.auction.guid = :auctionGuid")
    Long summaryCostLotsByAuctionGuid(@Param("auctionGuid") UUID auctionGuid);

    /**
     * Получение максимального порядково числа лотов при помощи аукциона
     *
     * @param auction сущность аукцион
     * @return Целое число, являющиеся максимальным порядковым число
     */
    @Query("select max(al.lotNumber) from AuctionLot al where al.auction = :auction")
    Integer findMaxNumber(Auction auction);

    /**
     * Получение Optional сущности при помощи сущностей: лот
     *
     * @param lot сущность лот
     * @return Optional-объект сущности AuctionLot
     */
    Optional<AuctionLot> findByLot(Lot lot);
}
