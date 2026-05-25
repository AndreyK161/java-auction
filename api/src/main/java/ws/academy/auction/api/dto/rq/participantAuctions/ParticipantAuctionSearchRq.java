package ws.academy.auction.api.dto.rq.participantAuctions;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ParticipantAuctionSearchRq {
    @Schema(description = "Номер страницы полученного списка лотов")
    @Min(value = 1, message = "Page number must be greater than or equal to 1")
    private Integer page = 1;

    @Schema(description = "Количество записей на странице полученного списка лотов")
    @Min(value = 30, message = "Count per page must be greater than or equal to 30")
    private Integer count = 30;

    @Schema(description = "Идентификатор аукциона, перечень заявок которого будут показан")
    private UUID guid;
}
