package ws.academy.auction.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ws.academy.auction.core.enums.ParticipantAuctionStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "auction_participants")
@IdClass(AuctionParticipantId.class)
public class ParticipantAuction {

    @Id
    @ManyToOne
    @JoinColumn(name = "participant_guid")
    private Participant participant;

    @Id
    @ManyToOne
    @JoinColumn(name = "auction_guid")
    private Auction auction;

    @Column(name = "participant_number", unique = true)
    private Integer participantNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ParticipantAuctionStatus status;

    @Column(name = "created_at")
    private LocalDateTime createAt;
}
