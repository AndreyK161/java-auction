package ws.academy.auction.api.dto.rs.participants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.lots.LotSummaryRs;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO, содержащее информацию об участнике, зарегистрированном на торги")
public class RegisteredParticipantRs {

    @Schema(description = "Идентификатор участника")
    private UUID id;

    @Schema(description = "Email участника")
    private String email;

    @Schema(description = "Полное имя участника")
    private String fullName;

    @Schema(description = "Лоты, выставленные на продажу")
    private List<LotSummaryRs> forSaleLots;

    @Schema(description = "Лоты, которые участник покупает")
    private List<LotSummaryRs> purchaseLots;

    @Schema(description = "Список доступных действий для участника")
    private List<String> actions;
}
