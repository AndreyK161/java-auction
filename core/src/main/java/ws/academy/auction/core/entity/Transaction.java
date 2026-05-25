package ws.academy.auction.core.entity;

import jakarta.persistence.*;
import lombok.*;
import ws.academy.auction.core.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transactions")
public class Transaction {
    @Id
    @Column(name = "guid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID guid;

    @Column(name = "at")
    private LocalDateTime at;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "comment")
    private String comment;

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "participant_guid")
    private Participant participant;
}
