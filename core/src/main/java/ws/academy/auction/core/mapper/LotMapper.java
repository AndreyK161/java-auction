package ws.academy.auction.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ws.academy.auction.api.dto.rq.auctions.AuctionLotsSearchRequest;
import ws.academy.auction.api.dto.rq.lots.CreateLotRq;
import ws.academy.auction.api.dto.rq.lots.LotSearchRequest;
import ws.academy.auction.api.dto.rs.ListPageData;
import ws.academy.auction.api.dto.rs.lots.LotDetails;
import ws.academy.auction.api.dto.rs.lots.LotListItem;
import ws.academy.auction.api.dto.rs.lots.PurchaseLotItem;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Lot;

/**
 * Маппер для сущности Lot
 */
@Mapper
public interface LotMapper {

    /**
     * Получение лота собранного маппером
     *
     * @param createLotRq объект - запрос передаваемый клиентом
     * @return Lot объект - объект-сущность таблицы lots
     */
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "buyer", ignore = true)
    @Mapping(target = "lotStatus", ignore = true)
    Lot buildLot(CreateLotRq createLotRq);

    /**
     * Получение dto-объекта сущности Lot собранного маппером
     *
     * @param lot объект - передаваемая сущность для маппинга в dto
     * @return LotDetails объект - dto-объект сущности Lot
     */
    @Mapping(source = "lotStatus", target = "tradeStatus")
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "currentAuction", ignore = true)
    LotDetails buildLotDetailsRs(Lot lot);

    /**
     * Получение dto-объекта сущности Lot
     *
     * @param lot объект - передаваемая сущность для маппинга в dto
     * @return LotListItem объект - dto-объект сущности Lot
     */
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "tradeStatus", ignore = true)
    LotListItem toLotListItem(Lot lot);

    /**
     * Получение dto-объекта сущности для пагинации
     *
     * @param request объект - запрос передаваемый клиентом
     * @param size целое число - количество элементов на странице
     * @param page объект - страница с данными, возвращаемая Spring Data,
     *             содержащая коллекцию объектов Lot и информацию о пагинации
     * @return ListPageData объект - dto-объект сущности Lot, содержащий результаты с пагинацией
     */
    ListPageData buildListPageData(LotSearchRequest request, int size, Page<Lot> page);

    /**
     * Получение dto-объекта сущности Lot для пагинации
     *
     * @param request объект - запрос передаваемый клиентом
     * @param size целое число - количество элементов на странице
     * @param page объект - страница с данными, возвращаемая Spring Data,
     *             содержащая коллекцию объектов Lot и информацию о пагинации
     * @return ListPageData объект - dto-объект сущности Lot, содержащий результаты с пагинацией
     */
    ListPageData buildListPageData(AuctionLotsSearchRequest request, int size, Page<AuctionLot> page);

    PurchaseLotItem buildPurchaseLotItem(Lot lot);
}
