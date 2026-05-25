package ws.academy.auction.core.service;

import org.springframework.stereotype.Service;
import ws.academy.auction.api.dto.rq.transactions.TransactionSearchRq;
import ws.academy.auction.api.dto.rs.transactions.TransactionList;

import java.util.UUID;

/**
 * Сервис по управлению транзакциями пользователя
 */
@Service
public interface TransactionService {

    /**
     * Получение информации о транзакциях определенного пользователя
     *
     * @param guid идентификатор участника
     * @param request dto-объект с информацией передаваемой пользователем
     * @return TransactionList список объектов, список dto-объектов для получения dto-объекта транзакции
     */
    TransactionList getTransactions(UUID guid, TransactionSearchRq request);
}
