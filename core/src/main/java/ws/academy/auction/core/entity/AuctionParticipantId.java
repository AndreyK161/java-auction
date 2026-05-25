package ws.academy.auction.core.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import jakarta.persistence.Embeddable;

@Embeddable
public class AuctionParticipantId implements Serializable {

    private UUID participant;
    private UUID auction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionParticipantId that = (AuctionParticipantId) o;
        return Objects.equals(participant, that.participant) && Objects.equals(auction, that.auction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participant, auction);
    }
}
