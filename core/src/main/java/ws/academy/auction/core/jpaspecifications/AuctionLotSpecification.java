package ws.academy.auction.core.jpaspecifications;

import org.springframework.data.jpa.domain.Specification;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.enums.LotStatus;

import java.util.UUID;

public class AuctionLotSpecification {
    public static Specification<AuctionLot> hasAuctionGuid(UUID auctionGuid) {
        return (root, query, cb) ->
                auctionGuid == null ? null : cb.equal(root.get("auction").get("guid"), auctionGuid);
    }

    public static Specification<AuctionLot> hasAuctionStatus(LotStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("lot").get("lotStatus"), status);
    }
}
