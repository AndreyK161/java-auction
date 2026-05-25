package ws.academy.auction.api.dto.rs.participants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для детального отображения участника")
public class ParticipantDetails {
    @Schema(description = "Идентификатор участника")
    private UUID guid;

    @Schema(description = "Email участника")
    private String email;

    @Schema(description = "Имя участника")
    private String fullName;

    @Schema(description = "Баланс участника")
    private Double balance;
}
