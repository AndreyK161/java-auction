package ws.academy.auction.api.dto.rq.bids;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для создания ставки на лот")
public class CreateBidRq {

    private BigDecimal amount;

    private Integer participantNumber;

    private ParticipantDetails participant;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime dateTime;
}

