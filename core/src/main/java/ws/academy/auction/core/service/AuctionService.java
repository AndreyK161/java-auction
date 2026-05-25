package ws.academy.auction.core.service;

import org.springframework.stereotype.Service;
import ws.academy.auction.api.dto.rq.auctions.AuctionCreateRq;
import ws.academy.auction.api.dto.rq.auctions.AuctionSearchRequest;
import ws.academy.auction.api.dto.rs.GuidRs;
import ws.academy.auction.api.dto.rs.auctions.AuctionRs;
import ws.academy.auction.api.dto.rs.auctions.AuctionList;

import java.util.UUID;

/**
 * Сервис по управлению с аукционами
 */
@Service
public interface AuctionService {
    /**
     * Создание аукциона
     *
     * @param startDate объект - объект-dto сущности Auction (является датой начала торгов)
     * @return GuidRs объект - объект-dto возвращаемая UUID сущности
     */
    GuidRs createAuction(AuctionCreateRq startDate);

    /**
     * Обновление данных аукциона
     *
     * @param startDate объект - объект-dto сущности Auction (является датой начала торгов)
     * @param guid идентификатор аукциона
     */
    void updateAuction(AuctionCreateRq startDate, UUID guid);

    /**
     * Удаление аукциона
     *
     * @param guid идентификатор аукциона
     */
    void deleteAuction(UUID guid);

    /**
     * Получение детальной информации выбранного аукциона
     *
     * @param guid идентификатор аукциона
     * @return AuctionRs объект - объект-dto сущности Auction
     */
    AuctionRs getAuctionWithId(UUID guid);

    /**
     * Действие с аукционом
     *
     * @param guid идентификатор аукциона
     * @param action выбранное действие пользователем
     */
    void actionAuction(UUID guid, String action);

    /**
     * Выставление лота на продажу [admin]
     *
     * @param guid идентификатор аукциона
     * @param lotGuid идентификатор лота
     */
    void setSellerLot(UUID guid, UUID lotGuid);

    /**
     * Получение перечня аукционов
     *
     * @param request запрос передаваемый клиентом
     * @return ListAuctionRs список объектов - список объектов-dto сущности Auction
     */
    AuctionList getAuctions(AuctionSearchRequest request);
}
