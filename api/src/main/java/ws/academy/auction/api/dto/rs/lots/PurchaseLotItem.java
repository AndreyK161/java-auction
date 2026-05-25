package ws.academy.auction.api.dto.rs.lots;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.photos.PhotoDetails;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Краткая информация о выигрышном лоте")
public class PurchaseLotItem {

    @Schema(description = "Идентификатор лота")
    private UUID guid;

    @Schema(description = "Название лота")
    private String title;

    @Schema(description = "Начальная цена")
    private BigDecimal startPrice;

    @Schema(description = "Фотографии лота")
    private List<PhotoDetails> photo;

    @Schema(description = "Информация об победителе")
    private PurchaseInfo purchaseInfo;

}
