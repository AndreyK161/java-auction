package ws.academy.auction.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "participants")
public class Participant {

    @Id
    @Column(name = "guid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID guid;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Lot> lotsOwner;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Lot> lotsBuyer;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private List<ParticipantAuction> participantAuctions;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Bid> bids;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_guid", referencedColumnName = "guid")
    private User user;
}
