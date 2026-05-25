package ws.academy.auction.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ws.academy.auction.core.entity.Auction;
import ws.academy.auction.core.entity.ParticipantAuction;
import ws.academy.auction.core.entity.Participant;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий по управлению участниками аукциона в базе данных
 */
public interface ParticipantAuctionRepository extends JpaRepository<ParticipantAuction, UUID>,
        JpaSpecificationExecutor<ParticipantAuction> {
    /**
     * Получение Optional-сущности ParticipantAuction
     *
     * @param auction объект-сущность Auction
     * @param participant объект-сущность Participant
     * @return Optional<ParticipantAuction> объект - Optional-сущность ParticipantAuction
     */
    Optional<ParticipantAuction> findAuctionParticipantByAuctionAndParticipant
            (Auction auction, Participant participant);

    /**
     * Получение максимального номера участника аукциона
     *
     * @param auction объект-сущность Auction
     * @return Номер участника аукциона
     */
    @Query("select max(pa.participantNumber) from ParticipantAuction pa where pa.auction = :auction")
    Integer findMaxNumber(@Param("auction") Auction auction);

    /**
     * Получение количества аукционов
     *
     * @param auctionGuid идентификатор аукциона
     * @return Количество аукционов
     */
    @Query("select count(pa) from ParticipantAuction pa where pa.auction.guid = :auctionGuid")
    long countByAuctionGuid(@Param("auctionGuid") UUID auctionGuid);

    Optional<ParticipantAuction> findAuctionParticipantByAuctionAndParticipantNumber(Auction auction, int number);
}
