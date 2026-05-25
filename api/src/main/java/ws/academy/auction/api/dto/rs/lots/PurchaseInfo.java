package ws.academy.auction.api.dto.rs.lots;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Краткая информация о победителе")
public class PurchaseInfo {

    @Schema(description = "Дата выигрыша")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime date;

    @Schema(description = "Информация об участнике")
    private ParticipantDetails participant;

    @Schema(description = "Стоимость покупки")
    private BigDecimal cost;
}
