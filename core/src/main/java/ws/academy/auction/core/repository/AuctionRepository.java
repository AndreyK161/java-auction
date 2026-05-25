package ws.academy.auction.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ws.academy.auction.core.entity.Auction;

import java.util.UUID;

/**
 * Репозиторий по управлению аукционами в базе данных
 */
@Repository
public interface AuctionRepository extends JpaRepository<Auction, UUID>, JpaSpecificationExecutor<Auction> {

    /**
     * Поиск максимального номера аукциона
     *
     * @return Номер аукциона
     */
    @Query("select max(a.number) from Auction a")
    Integer findMaxNumber();
}
