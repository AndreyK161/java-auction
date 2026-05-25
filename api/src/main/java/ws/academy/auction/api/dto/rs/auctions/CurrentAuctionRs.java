package ws.academy.auction.api.dto.rs.auctions;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO конкретного аукциона в котором участвует лот")
public class CurrentAuctionRs {

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime startAt;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime endAt;

    private int bidCount;

    private ParticipantDetails leader;
}
