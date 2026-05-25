package ws.academy.auction.api.dto.rq.users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для отправки имени пользователя")
@Getter
@Setter
public class FullNameRq {

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Schema(description = "Имя пользователя")
    private String fullName;
}
