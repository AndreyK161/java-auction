package ws.academy.auction.api.dto.rs.participants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.lots.LotSummaryRs;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для детального отображения участника аукциона")
public class ParticipantAuctionDetails {

    @Schema(description = "Номер участника аукциона")
    private Integer number;

    @Schema(description = "DTO участника")
    private ParticipantDetails participant;

    @Schema(description = "Список лотов выставленных на продажу")
    private List<LotSummaryRs> forSaleLots;

    @Schema(description = "Лоты участника доступные для добавления в аукцион")
    private List<LotSummaryRs> availableLots;
}
