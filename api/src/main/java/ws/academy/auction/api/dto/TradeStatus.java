package ws.academy.auction.api.dto;

import lombok.Getter;

@Getter
public enum TradeStatus {
    NEW ("Новый"),
    AUCTION_REQUEST ("Подан на регистрацию"),
    WAITING_FOR_TRADING ("Ждет торгов"),
    ON_TRADE ("Торгуется"),
    WITHDRAW_FROM_AUCTION ("Вывод с аукциона"),
    SOLD ("Продан");

    private final String tradeStatus;

    TradeStatus(String statusName) {
        this.tradeStatus = statusName;
    }
}
