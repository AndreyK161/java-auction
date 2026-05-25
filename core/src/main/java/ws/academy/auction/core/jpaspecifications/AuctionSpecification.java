package ws.academy.auction.core.jpaspecifications;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ws.academy.auction.core.entity.Auction;
import ws.academy.auction.core.entity.ParticipantAuction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AuctionSpecification {
    public static Specification<Auction> hasParticipants(List<UUID> participantGuids) {
        return (root, query, cb) -> {
            if (participantGuids == null || participantGuids.isEmpty()) {
                return null;
            }

            Join<Auction, ParticipantAuction> join = root.join("participantAuctionList");
            return join.get("participant").get("guid").in(participantGuids);
        };
    }

    public static Specification<Auction> hasStatuses(List<String> statuses) {
        return ((root, query, cb) -> {
            if (statuses == null || statuses.isEmpty()) {
                return null;
            }

            return root.get("status").in(statuses);
        });
    }

    public static Specification<Auction> hasFrom(LocalDateTime from) {
        return (root, query, cb) -> {
            if (from == null) {
                return null;
            }
            return cb.greaterThanOrEqualTo(root.get("startDate"), from);
        };
    }

    public static Specification<Auction> hasTo(LocalDateTime to) {
        return (root, query, cb) -> {
            if (to == null) {
                return null;
            }
            return cb.lessThanOrEqualTo(root.get("startDate"), to);
        };
    }
}
