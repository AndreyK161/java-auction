package ws.academy.auction.api.dto.rq.participants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для регистрации участника на торги")
public class ParticipateRq {

    @Schema(description = "Идентификатор торгов")
    private Integer auctionId;

    @Schema(description = "Сумма депозита для участия")
    private Integer depositAmount;
}
