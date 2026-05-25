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
@Schema(description = "DTO для создания нового пользователя")
public class CreateUserRq {

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Schema(description = "Имя пользователя")
    private String fullName;

    @NotBlank(message = "Email пользователя не может быть пустым")
    @Schema(description = "Email пользователя")
    private String email;

    @NotBlank(message = "Роль пользователя не может быть пустым")
    @Schema(description = "Роль пользователя")
    private String role;
}
