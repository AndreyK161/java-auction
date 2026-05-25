package ws.academy.auction.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import ws.academy.auction.api.dto.rq.transactions.TransactionSearchRq;
import ws.academy.auction.api.dto.rs.ListPageData;
import ws.academy.auction.api.dto.rs.transactions.TransactionListItem;
import ws.academy.auction.core.entity.Transaction;

@Mapper
public interface TransactionMapper {

    /**
     * Получение dto-объекта сущности Lot
     *
     * @param transaction объект - передаваемая сущность для маппинга в dto
     * @return TransactionListItem объект - dto-объект сущности TransactionRs
     */
    @Mapping(source = "at", target = "dateTime")
    TransactionListItem toTransactionListItem(Transaction transaction);

    /**
     * Получение dto-объекта сущности для пагинации
     *
     * @param request объект - запрос передаваемый клиентом
     * @param size целое число - количество элементов на странице
     * @param page объект - страница с данными, возвращаемая Spring Data,
     *             содержащая коллекцию объектов Lot и информацию о пагинации
     * @return ListPageData объект - dto-объект сущности Lot, содержащий результаты с пагинацией
     */
    ListPageData buildListPageData(TransactionSearchRq request, int size, Page<Transaction> page);
}
