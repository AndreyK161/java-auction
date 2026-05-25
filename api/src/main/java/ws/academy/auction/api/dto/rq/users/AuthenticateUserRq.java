package ws.academy.auction.api.dto.rq.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для аутентификации пользователя")
public class AuthenticateUserRq {

    @NotBlank(message = "Логин пользователя не может быть пустым")
    @Schema(description = "Логин пользователя")
    private String login;

    @NotBlank(message = "Пароль пользователя не может быть пустым")
    @Schema(description = "Пароль пользователя")
    private String password;
}
