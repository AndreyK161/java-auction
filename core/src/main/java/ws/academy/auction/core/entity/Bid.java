package ws.academy.auction.core.entity;

import jakarta.persistence.*;
import lombok.*;
import ws.academy.auction.core.enums.BidStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "bids")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Bid {

    @Id
    @Column(name = "guid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID guid;

    @ManyToOne
    @JoinColumn(name = "auction_lot_guid")
    private AuctionLot auctionLot;

    @ManyToOne
    @JoinColumn(name = "buyer_guid")
    private Participant buyer;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "bid_number")
    private Integer bidNumber;

    @Column(name = "status_bid")
    @Enumerated(EnumType.STRING)
    private BidStatus bidStatus;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

}


