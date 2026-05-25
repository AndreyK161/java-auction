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
@Schema(description = "DTO для отправки refresh токена")
public class RefreshTokenRq {

    @NotBlank(message = "Refresh Token пользователя не может быть пустым")
    @Schema(description = "Refresh Token пользователя")
    private String refreshToken;
}
