package ws.academy.auction.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "password_recoveries")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordRecovery {
    @Id
    @Column(name = "guid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID guid;

    @Column(name = "recovery_secret")
    private String recoverySecret;

    @ManyToOne
    @JoinColumn(name = "user_guid")
    private User userGuid;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}



