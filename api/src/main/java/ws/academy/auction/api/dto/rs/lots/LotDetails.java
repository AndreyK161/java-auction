package ws.academy.auction.api.dto.rs.lots;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.TradeStatus;
import ws.academy.auction.api.dto.rs.auctions.AuctionRs;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;
import ws.academy.auction.api.dto.rs.photos.PhotoDetails;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO с детальной информацией о лоте")
public class LotDetails {

    @Schema(description = "Идентификатор лота")
    private UUID guid;

    @Schema(description = "Наименование лота")
    private String title;

    @Schema(description = "Описание лота")
    private String description;

    @Schema(description = "Начальная цена лота")
    private Long startPrice;

    @Schema(description = "Шаг цены")
    private Long priceStep;

    @Schema(description = "Список dto-объектов фотографий лота")
    private List<PhotoDetails> photos;

    @Schema(description = "Информация об аукционе в котором участвует")
    private AuctionRs currentAuction;

    @Schema(description = "Статус лота")
    private TradeStatus tradeStatus;

    @Schema(description = "Владелец лота")
    private ParticipantDetails owner;

    @Schema(description = "Признак удаленности лота")
    private boolean deleted;
}
