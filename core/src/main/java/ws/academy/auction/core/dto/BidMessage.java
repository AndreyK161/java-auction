package ws.academy.auction.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO с данными о ставки
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BidMessage {

    private UUID guid;

    private UUID auctionId;

    private UUID lotId;

    private BigDecimal amount;

    private Integer participantNumber;
    private UUID participantId;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime dateTime;
}
