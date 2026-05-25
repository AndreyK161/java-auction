package ws.academy.auction.api.dto.rq.lots;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rq.participants.ParticipantUUIDRq;
import ws.academy.auction.api.dto.rq.photos.LotPhotoUUIDRq;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для создания нового лота")
public class CreateLotRq {

    @NotBlank(message = "Имя лота не может быть пустым")
    @Schema(description = "Наименование лота")
    private String title;

    @Schema(description = "Описание лота")
    private String description;

    @NotNull(message = "Начальная цена не может быть пустой")
    @Min(value = 1, message = "Начальная цена начинается от 1")
    @Schema(description = "Начальная цена лота")
    private BigDecimal startPrice;

    @NotNull(message = "Шаг цены не может быть пустым")
    @PositiveOrZero(message = "Число должно быть положительным")
    @Schema(description = "Шаг цены")
    private BigDecimal priceStep;

    @Schema(description = "Идентификаторы фото лота")
    private List<LotPhotoUUIDRq> photo;

    @NotNull(message = "Идентификатор продавца не может быть пустым")
    @Schema(description = "Идентификатор продавца лота")
    private ParticipantUUIDRq owner;
}
