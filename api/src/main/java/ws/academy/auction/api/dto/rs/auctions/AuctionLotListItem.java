package ws.academy.auction.api.dto.rs.auctions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.bids.BidRs;
import ws.academy.auction.api.dto.rs.lots.LotDetails;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для списка лотов участвующих в аукционе")
public class AuctionLotListItem {

    @Schema(description = "Номер аукциона в котором участвует лот")
    private Integer number;

    @Schema(description = "Статус лота")
    private String status;

    @Schema(description = "DTO лота")
    private LotDetails lot;
    @Schema(description = "Информация о последней ставке")
    private BidRs lastBid;

    @Schema(description = "Количество ставок")
    private Integer bidCount = 0;

    @Schema(description = "Детальная информация о победителе")
    private ParticipantDetails winner;

    @Schema(description = "Итоговая сумма ставок")
    private BigDecimal totalAmount;

    @Schema(description = "Список действий пользователя")
    private List<String> actions;
}
