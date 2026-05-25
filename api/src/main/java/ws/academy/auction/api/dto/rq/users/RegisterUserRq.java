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
@Schema(description = "DTO для регистрации пользователя")
public class RegisterUserRq {

    @NotBlank(message = "Email пользователя не может быть пустым")
    @Schema(description = "Email пользователя")
    private String email;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Schema(description = "Имя пользователя")
    private String fullName;
}
