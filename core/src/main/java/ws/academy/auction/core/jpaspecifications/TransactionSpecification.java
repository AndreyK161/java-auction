package ws.academy.auction.core.jpaspecifications;

import org.springframework.data.jpa.domain.Specification;
import ws.academy.auction.core.entity.Transaction;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionSpecification {

    public static Specification<Transaction> hasFrom(LocalDateTime from) {
        return (root, query, cb) -> {
            if (from == null) {
                return null;
            }
            return cb.greaterThanOrEqualTo(root.get("at"), from);
        };
    }

    public static Specification<Transaction> hasTo(LocalDateTime to) {
        return (root, query, cb) -> {
            if (to == null) {
                return null;
            }
            return cb.lessThanOrEqualTo(root.get("at"), to);
        };
    }

    public static Specification<Transaction> hasParticipantGuid(UUID participantGuid) {
        return (root, query, cb) ->
                participantGuid == null ? null : cb.equal(root.get("participant").get("guid"), participantGuid);
    }
}
