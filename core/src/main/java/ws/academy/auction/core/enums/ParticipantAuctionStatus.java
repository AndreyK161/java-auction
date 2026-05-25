package ws.academy.auction.core.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ParticipantAuctionStatus {
    NEW ("Новый"),
    ACCEPTED ("Принятый"),
    DECLINED ("Отмененный");

    private final String statusName;

    ParticipantAuctionStatus(String statusName) {
        this.statusName = statusName;
    }

    public static ParticipantAuctionStatus from(String name) {
        return Arrays.stream(values())
                .filter(f -> f.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(NEW);
    }
}
