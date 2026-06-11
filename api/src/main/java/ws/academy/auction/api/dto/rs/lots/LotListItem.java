package ws.academy.auction.api.dto.rs.lots;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.photos.PhotoDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для списка лотов")
public class LotListItem {

    @Schema(description = "Идентификатор лота")
    private UUID guid;

    @Schema(description = "Наименование лота")
    private String title;

    @Schema(description = "Начальная цена лота")
    private Long startPrice;

    @Schema(description = "Время и дата создания лота")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Список идентификаторов фотографий лота")
    private List<PhotoDetails> photos;

    @Schema(description = "Статус лота")
    private String tradeStatus;
}
