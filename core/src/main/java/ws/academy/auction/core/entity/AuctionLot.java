package ws.academy.auction.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "auction_lots")
public class AuctionLot {

    @Id
    @Column(name = "guid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID guid;

    @ManyToOne
    @JoinColumn(name = "auction_guid")
    private Auction auction;

    @ManyToOne
    @JoinColumn(name = "lot_guid")
    private Lot lot;

    @Column(name = "lot_number")
    private Integer lotNumber;

    @OneToMany(mappedBy = "auctionLot")
    private List<Bid> bid;
}
