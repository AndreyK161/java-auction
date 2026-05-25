package ws.academy.auction.core.messages.impl;

import org.springframework.stereotype.Component;
import ws.academy.auction.core.dto.BidStatusMessage;
import ws.academy.auction.core.messages.BidResultGateway;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BidResultGatewayImpl implements BidResultGateway {
    private final Map<UUID, CompletableFuture<BidStatusMessage>> bidFutures = new ConcurrentHashMap<>();

    @Override
    public void register(UUID guid, CompletableFuture<BidStatusMessage> future) {
        bidFutures.put(guid, future);
    }

    @Override
    public void complete(UUID guid, BidStatusMessage result) {
        CompletableFuture<BidStatusMessage> future = bidFutures.remove(guid);
        if (future != null) {
            future.complete(result);
        }
    }
}