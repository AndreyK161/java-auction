package ws.academy.auction.api.dto;

import lombok.Getter;

@Getter
public enum AuctionStatus {
    PUBLISHED ("Опубликован"),
    ACTIVE ("Активный"),
    ARCHIVE ("Завершенный");

    private final String code;

    AuctionStatus(String code) {
        this.code = code;
    }

}
