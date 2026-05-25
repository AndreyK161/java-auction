package ws.academy.auction.core.jpaspecifications;

import org.springframework.data.jpa.domain.Specification;
import ws.academy.auction.core.entity.Auction;
import ws.academy.auction.core.entity.ParticipantAuction;
import ws.academy.auction.core.enums.ParticipantAuctionStatus;

public class ParticipantAuctionSpecification {

    public static Specification<ParticipantAuction> byAuction(Auction auction) {
        return (root, query, cb) -> cb.equal(root.get("auction"), auction);
    }

    public static Specification<ParticipantAuction> byStatus(ParticipantAuctionStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
