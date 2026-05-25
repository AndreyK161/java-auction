package ws.academy.auction.api.dto.rs.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO JWT токена для ответа")
public class JwtRs {

    @Schema(description = "Access токен")
    private String accessToken;

    @Schema(description = "Refresh токен")
    private String refreshToken;
}
