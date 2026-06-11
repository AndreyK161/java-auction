package ws.academy.auction.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ws.academy.auction.api.dto.rq.transactions.TransactionSearchRq;
import ws.academy.auction.api.dto.rs.ListPageData;
import ws.academy.auction.api.dto.rs.transactions.TransactionList;
import ws.academy.auction.api.dto.rs.transactions.TransactionListItem;
import ws.academy.auction.core.entity.Transaction;
import ws.academy.auction.core.jpaspecifications.TransactionSpecification;
import ws.academy.auction.core.mapper.TransactionMapper;
import ws.academy.auction.core.repository.TransactionRepository;
import ws.academy.auction.core.service.TransactionService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionList getTransactions(UUID guid, TransactionSearchRq request) {
        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getCount() != null ? request.getCount() : 30;

        Pageable pageable = getPageable(page, size);

        Specification<Transaction> spec = getSpecification(guid,
                request.getDateFrom() != null ? request.getDateFrom().atStartOfDay() : null,
                request.getDateTo() != null ? request.getDateTo().atTime(LocalTime.MAX) : null);

        Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);

        List<TransactionListItem> items = createTransactionListItem(transactionPage);

        ListPageData pageData = createListPageData(request, size, transactionPage);

        return createTransactionList(items, pageData);
    }

    private Pageable getPageable(int page, int size) {
        return PageRequest.of(page, size);
    }

    private Specification<Transaction> getSpecification(UUID guid, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return Specification
                .where(TransactionSpecification.hasParticipantGuid(guid))
                .and(TransactionSpecification.hasFrom(dateFrom))
                .and(TransactionSpecification.hasTo(dateTo));
    }

    private List<TransactionListItem> createTransactionListItem(Page<Transaction> transactionPage) {
        return transactionPage.getContent().stream()
                .map(transaction -> {
                    TransactionListItem item = transactionMapper.toTransactionListItem(transaction);
                    item.setDateTime(transaction.getAt());
                    return item;
                })
                .toList();
    }

    private ListPageData createListPageData(
            TransactionSearchRq request, int size, Page<Transaction> transactionPage) {
        return transactionMapper.buildListPageData(request, size, transactionPage);
    }

    private TransactionList createTransactionList(List<TransactionListItem> transactionListItem,
                                                  ListPageData pageData) {
        return TransactionList.builder()
                .items(transactionListItem)
                .page(pageData)
                .build();
    }
}
