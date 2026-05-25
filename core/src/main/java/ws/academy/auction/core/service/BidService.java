package ws.academy.auction.core.service;

import org.springframework.stereotype.Service;
import ws.academy.auction.api.dto.rq.bids.CreateBidRq;
import ws.academy.auction.api.dto.rs.bids.BidList;
import ws.academy.auction.core.dto.CreateBidRs;

import java.util.UUID;

/**
 * Сервис по управлению ставками
 */
@Service
public interface BidService {

    /**
     * Добавление ставки на лот
     *
     * @param auctionId идентификатор аукциона
     * @param lotId идентификатор лота
     * @param request запрос с данными от пользователя
     * @return CreateBidRs объект, dto-объект для получения dto-объекта ставки
     */
    CreateBidRs addLotBid(UUID auctionId, UUID lotId, CreateBidRq request);

    /**
     * Получение информации о ставках в определенном аукционе и лоте
     *
     * @param auctionId идентификатор аукциона
     * @param lotId идентификатор лота
     * @return BidList список объектов, список dto-объектов для получения dto-объекта ставки
     */
    BidList getBids(UUID auctionId, UUID lotId);
}
