package ws.academy.auction.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO, содержащее данные о ставке")
public class LotBid {

    @NotNull
    @Min(1)
    @Schema(description = "Сумма ставки")
    private Integer amount;

    @NotBlank
    @Schema(description = "Email участника")
    private String participantEmail;

    @NotNull
    @Schema(description = "Дата и время совершения ставки")
    private LocalDateTime dateTime;
}
