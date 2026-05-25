package ws.academy.auction.core.jpaspecifications;

import org.springframework.data.jpa.domain.Specification;
import ws.academy.auction.core.entity.User;
import ws.academy.auction.core.enums.UserRole;

public class UserSpecification {
    public static Specification<User> hasName(String name) {
        return (root, query, cb) ->
                (name == null || name.isBlank())
                        ? null
                        : cb.like(cb.lower(root.get("username")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<User> hasRole(String role) {
        return (root, query, cb) ->
                (role == null || role.isBlank())
                        ? null
                        : cb.equal(root.get("role"), UserRole.valueOf(role.toUpperCase()));
    }
}
