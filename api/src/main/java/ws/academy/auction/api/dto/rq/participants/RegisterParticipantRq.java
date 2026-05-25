package ws.academy.auction.api.dto.rq.participants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для регистрации нового участника")
public class RegisterParticipantRq {

    @Schema(description = "Email участника")
    private String email;

    @Schema(description = "Логин участника")
    private String userName;
}
