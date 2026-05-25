package ws.academy.auction.core.enums;

import lombok.Getter;

@Getter
public enum BidStatus {
    ACCEPTED("Принята"),
    OUTBID("Перебита"),
    REJECTED("Отклонена");

    private final String name;

    BidStatus(String name) {
        this.name = name;
    }
}
