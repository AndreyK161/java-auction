package ws.academy.auction.core.messages;

import ws.academy.auction.core.dto.BidStatusMessage;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface BidResultGateway {
    void register(UUID guid, CompletableFuture<BidStatusMessage> future);

    void complete(UUID guid, BidStatusMessage result);
}
