package ws.academy.auction.core.service;

import ws.academy.auction.api.dto.rq.auctions.AuctionLotsSearchRequest;
import ws.academy.auction.api.dto.rq.lots.LotGuidRq;
import ws.academy.auction.api.dto.rs.auctions.AuctionLotList;

import java.util.UUID;

public interface AuctionLotService {
    /**
     * Получение списка лотов зарегистрированных в торгах с использованием фильтров
     *
     * @param guid идентификатор аукциона
     * @param request - запрос передаваемый клиентом с нужными фильтрами
     *
     * @return AuctionList объект - dto-объект списка лотов зарегистрированных в торгах
     */
    AuctionLotList getLotsByAuctionId(UUID guid, AuctionLotsSearchRequest request);

    /**
     * Удаление зарегистрированного лота с торгов
     *
     * @param guid идентификатор аукциона
     * @param lotGuid идентификатор лота
     */
    void deleteAuctionLot(UUID guid, LotGuidRq lotGuid);

    /**
     * Регистрация лота на торгах
     *
     * @param guid идентификатор аукциона
     * @param lotGuid идентификатор лота
     */
    void addAuctionLot(UUID guid, LotGuidRq lotGuid);

    /**
     * Удаление зарегистрированного лота с торгов [admin]
     *
     * @param guid идентификатор аукциона
     * @param lotGuid идентификатор лота
     */
    void deleteAuctionLot(UUID guid, UUID lotGuid);

    /**
     * Регистрация лота на торгах [admin]
     *
     * @param guid идентификатор аукциона
     * @param lotGuid идентификатор лота
     */
    void addAuctionLot(UUID guid, UUID lotGuid);
}
