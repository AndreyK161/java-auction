package ws.academy.auction.api.dto.rq.lots;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ws.academy.auction.api.dto.rq.photos.LotPhotoUUIDRq;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "DTO для обновления определенного лота")
public class UpdateLotRq {

    @NotNull(message = "Название лота не может быть пустым")
    @Schema(description = "Наименование лота")
    private String title;

    @Schema(description = "Описание лота")
    private String description;

    @NotNull(message = "Начальная цена не может быть пустой")
    @Min(value = 1, message = "Начальная цена лота начинается с 1")
    @Schema(description = "Начальная цена лота")
    private BigDecimal startPrice;

    @Schema(description = "Идентификаторы фото лота")
    private List<LotPhotoUUIDRq> photo;
}
