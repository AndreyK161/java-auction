package ws.academy.auction.api.dto.rs.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO детальной информации по пользователю")
public class UserDetails {

    @Schema(description = "Идентификатор пользователя")
    private UUID guid;

    @Schema(description = "Email пользователя")
    private String email;

    @Schema(description = "Имя пользователя")
    private String fullName;

    @Schema(description = "Роль пользователя")
    private String role;

    @Schema(description = "Информация об пользователе как участника")
    private ParticipantDetails participant;
}
