package ws.academy.auction.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import ws.academy.auction.core.enums.UserRole;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
@SQLDelete(sql = "update users set deleted=true where guid=?")
@Where(clause = "deleted = false")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "guid", unique = true)
    private UUID guid;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean deleted;

    @Column(name = "activated", nullable = false, columnDefinition = "boolean default false")
    private boolean activated;

    @Column(name = "hash")
    private String hash;

    @Column(name = "guid_keycloak")
    private UUID guidKeycloak;
}
