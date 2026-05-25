package ws.academy.auction.api.dto.rs.auctions;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для списка аукционов")
public class AuctionListItem {
    @Schema(description = "Идентификатор аукциона")
    private UUID guid;

    @Schema(description = "Номер аукциона")
    private Integer number;

    @Schema(description = "Дата создания торгов")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime startDate;

    @Schema(description = "Статус торгов")
    private AuctionStatusDto status;

    @Schema(description = "Количество лотов аукциона")
    private Long lotsCount;

    @Schema(description = "Количество участников аукциона")
    private Long participantCount;

    @Schema(description = "Бюджет аукциона")
    private Long budget;

    @Schema(description = "Участие в аукционе")
    private boolean participation;

    private List<String> actions;


}
