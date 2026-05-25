package ws.academy.auction.api.dto.rs.lots;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.auctions.AuctionShortRs;
import ws.academy.auction.api.dto.rs.photos.PhotoDetails;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Краткая информация о лоте участника")
public class LotSummaryRs {

    @Schema(description = "Идентификатор лота")
    private UUID guid;

    @Schema(description = "Название лота")
    private String title;

    @Schema(description = "Начальная цена лота")
    private BigDecimal startPrice;

    @Schema(description = "Дата и время создание лота")
    private String createdAt;

    @Schema(description = "Идентификатор аукциона в котором последний раз участвовал лот")
    private AuctionShortRs lastAuction;

    @Schema(description = "Фотографии лота")
    private List<PhotoDetails> photo;

    @Schema(description = "Список возможных действий")
    private List<String> actions;
}
