package ws.academy.auction.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Bid;
import ws.academy.auction.core.enums.BidStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<Bid, UUID> {
    /**
     * Получение всех ставок при помощи сущности AuctionLot отсортированном по убыванию
     *
     * @param auctionLot сущность AuctionLot
     * @return список всех ставок
     */
    List<Bid> findAllByAuctionLotOrderByStartAtDesc(AuctionLot auctionLot);

    /**
     * Поиск максимального номера покупателя
     *
     * @param auctionLot сущность AuctionLot
     * @return Номер покупателя
     */
    @Query("select max(b.bidNumber) from Bid b where b.auctionLot = :auctionLot")
    Integer findMaxNumber(AuctionLot auctionLot);

    /**
     * Получение Option-объекта сущности Bid при помощи номера покупателя и сущности AuctionLot
     *
     * @param number номер покупателя
     * @param auctionLot сущность AuctionLot
     * @return Optional-объект сущности Bid
     */
    Optional<Bid> findByBidNumberAndAuctionLot(Integer number, AuctionLot auctionLot);

    /**
     * Получение списка сущности Bid при помощи сущности AuctionLot и статусу ставки
     *
     * @param auctionLot сущность AuctionLot
     * @param status статус ставки
     * @return Список сущности Bid
     */
    List<Bid> findAllByAuctionLotAndBidStatus(AuctionLot auctionLot, BidStatus status);

    /**
     * Получение количества ставок по определенному AuctionLot
     *
     * @param auctionLot сущность AuctionLot
     * @return Количество ставок сделанных на определенный AuctionLot
     */
    @Query("select count(b) from Bid b where b.auctionLot = :auctionLot")
    Integer countBidsByAuctionLot(AuctionLot auctionLot);

    /**
     * Получение суммы ставок сделанных на определенный AuctionLot
     *
     * @param auctionLot сущность AuctionLot
     * @return Сумма всех ставок сделанных на определенный AcutionLot
     */
    @Query("select sum(b.amount) from Bid b where b.auctionLot = :auctionLot")
    BigDecimal sumBidsByAuctionLot(AuctionLot auctionLot);
}
