package ws.academy.auction.api.dto.rq.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "DTO для обновления определенного пользователя")
public class UpdateUserRq {

    @NotNull(message = "Имя пользователя не может быть пустым")
    @Schema(description = "Имя пользователя")
    private String fullName;

    @NotBlank(message = "Роль пользователя не может быть пустым")
    @Schema(description = "Роль пользователя")
    private String role;
}
