package ws.academy.auction.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import ws.academy.auction.core.enums.LotStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "lots")
@SQLDelete(sql = "update lots set deleted=true where guid=?")
@Where(clause = "deleted = false")
public class Lot {

    @Id
    @Column(name = "guid", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID guid;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "start_price", nullable = false)
    private BigDecimal startPrice;

    @Column(name = "price_step", nullable = false)
    private BigDecimal priceStep;

    @Column(name = "deleted", columnDefinition = "boolean default false", nullable = false)
    private boolean deleted;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_guid")
    private Participant owner;

    @ManyToOne
    @JoinColumn(name = "buyer_guid")
    private Participant buyer;

    @OneToMany(mappedBy = "lot", cascade = CascadeType.ALL)
    private List<AuctionLot> auctionLot;

    @Column(name = "lot_status")
    @Enumerated(EnumType.STRING)
    private LotStatus lotStatus;
}
