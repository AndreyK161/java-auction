package ws.academy.auction.core.enums;

import lombok.Getter;

@Getter
public enum LotStatus {
    NEW ("Новый"),
    AUCTION_REQUEST ("Подан на регистрацию"),
    WAITING_FOR_TRADING ("Ждет торгов"),
    ON_TRADE ("Торгуется"),
    WITHDRAW_FROM_AUCTION ("Вывод с аукциона"),
    SOLD ("Продан");

    private final String statusName;

    LotStatus(String statusName) {
        this.statusName = statusName;
    }
}
