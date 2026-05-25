package ws.academy.auction.core.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum LotSortField {
    TITLE("lot.title"),
    MIN_PRICE("lot.startPrice"),
    NUMBER("lotNumber");

    private final String entityField;

    LotSortField(String entityField) {
        this.entityField = entityField;
    }

    public static LotSortField from(String name) {
        return Arrays.stream(values())
                .filter(f -> f.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(TITLE);
    }
}
