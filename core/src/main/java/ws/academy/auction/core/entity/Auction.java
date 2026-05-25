package ws.academy.auction.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ws.academy.auction.api.dto.AuctionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "auctions")
public class Auction {

    @Id
    @Column(name = "guid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID guid;

    @Column(name = "number", unique = true)
    private Integer number;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AuctionStatus status;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private List<AuctionLot> auctionLot;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private List<ParticipantAuction> participantAuctionList;
}
