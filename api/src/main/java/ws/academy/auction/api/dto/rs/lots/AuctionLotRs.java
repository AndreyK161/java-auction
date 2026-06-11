package ws.academy.auction.api.dto.rs.lots;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.bids.BidRs;
import ws.academy.auction.api.dto.rs.photos.PhotoDetails;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Лот, участвующий в торгах")
public class AuctionLotRs {

    @Schema(description = "Идентификатор лота")
    private UUID guid;

    @Schema(description = "Наименование лота")
    private String title;

    @Schema(description = "Начальная цена")
    private BigDecimal startPrice;

    @Schema(description = "Список фотографий")
    private List<PhotoDetails> photo;

    @Schema(description = "Информация о последней ставке")
    private BidRs lastBid;

    @Schema(description = "Количество ставок")
    private Integer bidCount = 0;

    @Schema(description = "Статус лота")
    private String status;

    @Schema(description = "Номер лота в аукционе")
    private Integer number;
}
