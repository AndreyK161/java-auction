package ws.academy.auction.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ws.academy.auction.api.controller.ParticipantController;
import ws.academy.auction.api.dto.rq.lots.PurchaseLotsSearchRq;
import ws.academy.auction.api.dto.rq.participants.*;
import ws.academy.auction.api.dto.rq.transactions.TransactionSearchRq;
import ws.academy.auction.api.dto.rs.lots.PurchaseLotsList;
import ws.academy.auction.api.dto.rs.participants.*;
import ws.academy.auction.api.dto.rs.transactions.TransactionList;
import ws.academy.auction.core.service.ParticipantService;
import ws.academy.auction.core.service.TransactionService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ParticipantControllerImpl implements ParticipantController {
    private final ParticipantService participantService;
    private final TransactionService transactionService;

    @Override
    public PurchaseLotsList getPurchaseLots(UUID guid, PurchaseLotsSearchRq request) {
        return participantService.getPurchaseLots(guid, request);
    }

    @Override
    public OperationAccountWalletRs addAmountWallet(UUID guid, CreateOperationWalletRq request) {
        return participantService.addAmountWallet(guid, request);
    }

    @Override
    public OperationAccountWalletRs getWallet(UUID guid) {
        return participantService.getWallet(guid);
    }

    @Override
    public void putAmountWallet(UUID guid, CreateOperationAddAmountWalletRq request) {
        participantService.putAmountWallet(guid, request);
    }

    @Override
    public void withdrawAmount(UUID guid, CreateOperationWithdrawAmountRq request) {
        participantService.withdrawAmount(guid, request);
    }

    @Override
    public TransactionList getTransactions(UUID guid, TransactionSearchRq request) {
        return transactionService.getTransactions(guid, request);
    }
}
