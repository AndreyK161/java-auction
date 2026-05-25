package ws.academy.auction.core.service;

import org.springframework.stereotype.Service;
import ws.academy.auction.api.dto.rq.lots.CreateLotRq;
import ws.academy.auction.api.dto.rq.lots.LotSearchRequest;
import ws.academy.auction.api.dto.rq.lots.UpdateLotRq;
import ws.academy.auction.api.dto.rs.lots.LotDetails;
import ws.academy.auction.api.dto.rs.lots.LotList;

import java.util.UUID;

/**
 * Сервис по управлению лотами аукциона
 */

@Service
public interface LotService {
    /**
     * Создание лота
     *
     * @param createLotRq DTO с данными для создания лота
     * @return созданный лот
     */
    LotDetails createLot(CreateLotRq createLotRq);

    /**
     * Получение лота по идентификатору
     *
     * @param guid идентификатор лота
     * @return найденный лот
     */
    LotDetails getLotById(UUID guid);

    /**
     * Получение списка лотов с использованием фильтров
     *
     * @param request объект, содержащий фильтры
     * @return LotList - объект, список лотов с указанием переданных фильтров
     */
    LotList getLots(LotSearchRequest request);

    /**
     * Получение обновленного лота по идентификатору
     *
     * @param updateLotRq объект, содержащий данные для обновления существующего лота
     * @param guid идентификатор лота
     * @return LotDetails - объект, обновленный лот
     */
    LotDetails updateLot(UpdateLotRq updateLotRq, UUID guid);

    /**
     * Удаление лота (без фактического удаления из бд)
     *
     * @param guid идентификатор лота
     */
    void deleteLot(UUID guid);
}
