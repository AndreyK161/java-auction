package ws.academy.auction.core.service;

import org.springframework.stereotype.Service;
import ws.academy.auction.api.dto.rq.lots.PurchaseLotsSearchRq;
import ws.academy.auction.api.dto.rq.participants.CreateOperationAddAmountWalletRq;
import ws.academy.auction.api.dto.rq.participants.CreateOperationWalletRq;
import ws.academy.auction.api.dto.rq.participants.CreateOperationWithdrawAmountRq;
import ws.academy.auction.api.dto.rs.lots.PurchaseLotsList;
import ws.academy.auction.api.dto.rs.participants.OperationAccountWalletRs;

import java.util.UUID;

@Service
public interface ParticipantService {
    OperationAccountWalletRs addAmountWallet(UUID guid, CreateOperationWalletRq request);

    OperationAccountWalletRs getWallet(UUID guid);

    void putAmountWallet(UUID guid, CreateOperationAddAmountWalletRq request);

    void withdrawAmount(UUID guid, CreateOperationWithdrawAmountRq request);

    PurchaseLotsList getPurchaseLots(UUID guid, PurchaseLotsSearchRq request);
}
