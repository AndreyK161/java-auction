package ws.academy.auction.api.dto.rs.auctions;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.lots.AuctionLotRs;
import ws.academy.auction.api.dto.rs.participants.RegisteredParticipantRs;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO с данными о торгах")
public class AuctionRs {

    @Schema(description = "Идентификатор торгов")
    private UUID id;

    @Schema(description = "Индивидуальный номер торгов")
    private Integer number;

    @Schema(description = "Дата и время начала торгов")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime startDate;

    @Schema(description = "Статус торгов")
    private AuctionStatusDto status;

    @Schema(description = "Список лотов, участвующих в торгах")
    private List<AuctionLotRs> tradingLots;

    @Schema(description = "Список зарегистрированных участников")
    private List<RegisteredParticipantRs> participants;
}
