package ws.academy.auction.api.dto.rq.participants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для пополнения баланса участника")
public class CreateOperationAddAmountWalletRq {
    private BigDecimal amount;
    private CardRq cardRq;
}
