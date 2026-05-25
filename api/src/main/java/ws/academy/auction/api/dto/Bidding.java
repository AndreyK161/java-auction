package ws.academy.auction.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.lots.LotDetails;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO, содержащее данные о торгах по лоту")
public class Bidding {

    @Schema(description = "Данные лота")
    private LotDetails lot;

    @Schema(description = "Дата и время начала торгов по лоту")
    private LocalDateTime auctionStartDateTime;

    @Schema(description = "Минимальный \"шаг\" аукциона")
    private Integer stepAmount;

    @Schema(description = "Список ставок по лоту")
    private List<LotBid> lotBidList;

    @Schema(description = "Признак того, что торги по лоту завершены")
    private boolean isEnded;
}
