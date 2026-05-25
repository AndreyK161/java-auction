package ws.academy.auction.core.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ParticipantSortField {
    NUMBER("participantNumber"),
    FULL_NAME("participant.fullName");

    private final String sortField;

    ParticipantSortField(String sortField) {
        this.sortField = sortField;
    }

    public static ParticipantSortField from(String name) {
        return Arrays.stream(values())
                .filter(f -> f.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(FULL_NAME);
    }
}
