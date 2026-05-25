package ws.academy.auction.core.jpaspecifications;

import org.springframework.data.jpa.domain.Specification;
import ws.academy.auction.core.entity.Lot;
import ws.academy.auction.core.entity.Participant;
import ws.academy.auction.core.enums.LotStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class LotSpecification {
    public static Specification<Lot> hasName(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("title")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Lot> priceGreaterThanOrEqualTo(BigDecimal priceFrom) {
        return (root, query, cb) ->
                priceFrom == null ? null : cb.ge(root.get("startPrice"), priceFrom);
    }

    public static Specification<Lot> priceLessThanOrEqualTo(BigDecimal priceTo) {
        return (root, query, cb) ->
                priceTo == null ? null : cb.le(root.get("startPrice"), priceTo);
    }

    public static Specification<Lot> isDeleted(boolean showDeleted) {
        return (root, query, cb) ->
                showDeleted ? null : cb.isFalse(root.get("deleted"));
    }

    public static Specification<Lot> hasBuyerLot(Participant buyer) {
        return (root, query, cb) ->
                buyer == null ? null : cb.equal(root.get("buyer"), buyer);
    }

    public static Specification<Lot> hasStatus(LotStatus status) {
        return (root, query, cb) -> cb.equal(root.get("lotStatus"), status);
    }

    public static Specification<Lot> hasOwnerGuids(List<UUID> ownersGuid) {
        return (root, query, cb) -> {
            if (ownersGuid == null || ownersGuid.isEmpty()) {
                return null;
            }
            return root.get("owner").get("guid").in(ownersGuid);
        };
    }
}
