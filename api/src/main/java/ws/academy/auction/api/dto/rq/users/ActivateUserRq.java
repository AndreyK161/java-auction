package ws.academy.auction.api.dto.rq.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.validation.ValidPassword;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для активации аккаунта пользователя")
public class ActivateUserRq {

    @NotBlank(message = "Email пользователя не может быть пустым")
    @Schema(description = "Email пользователя")
    private String email;

    private String hash;

    @NotBlank(message = "Пароль пользователя не может быть пустым")
    @Schema(description = "Пароль пользователя")
    @ValidPassword
    private String password;
}
