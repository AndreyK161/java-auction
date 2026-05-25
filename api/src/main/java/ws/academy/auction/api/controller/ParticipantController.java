package ws.academy.auction.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ws.academy.auction.api.dto.rq.lots.PurchaseLotsSearchRq;
import ws.academy.auction.api.dto.rq.participants.CreateOperationAddAmountWalletRq;
import ws.academy.auction.api.dto.rq.participants.CreateOperationWalletRq;
import ws.academy.auction.api.dto.rq.participants.CreateOperationWithdrawAmountRq;
import ws.academy.auction.api.dto.rq.transactions.TransactionSearchRq;
import ws.academy.auction.api.dto.rs.lots.PurchaseLotsList;
import ws.academy.auction.api.dto.rs.participants.OperationAccountWalletRs;
import ws.academy.auction.api.dto.rs.transactions.TransactionList;

import java.util.UUID;

@Validated
@RequestMapping({"/api/v1/participants/", "/api/v1/participant/"})
@Tag(name = "ParticipantController", description = "Контроллер для работы с участниками")
public interface ParticipantController {

    @Operation(summary = "Список выигранных лотов у участника")
    @RequestMapping("{guid}/purchases")
    @GetMapping()
    PurchaseLotsList getPurchaseLots(@PathVariable UUID guid, @ModelAttribute PurchaseLotsSearchRq request);

    @Operation(summary = "Пополнение личного счета участника")
    @PostMapping("{guid}/account/")
    OperationAccountWalletRs addAmountWallet(@PathVariable UUID guid, @RequestBody CreateOperationWalletRq request);

    @Operation(summary = "Получение детальной информации по счету участника")
    @GetMapping("{guid}/account/")
    OperationAccountWalletRs getWallet(@PathVariable UUID guid);

    @Operation(summary = "Пополнить баланс")
    @PutMapping("{guid}/account/")
    void putAmountWallet(@PathVariable UUID guid, @RequestBody CreateOperationAddAmountWalletRq request);

    @Operation(summary = "Вывод средств на карту")
    @PostMapping("{guid}/account/withdraw/")
    void withdrawAmount(@PathVariable UUID guid, @RequestBody CreateOperationWithdrawAmountRq request);

    @Operation(summary = "Перечень транзакций личного счета участника")
    @GetMapping("{guid}/account/transactions/")
    TransactionList getTransactions(@PathVariable UUID guid, @ModelAttribute TransactionSearchRq request);
}
