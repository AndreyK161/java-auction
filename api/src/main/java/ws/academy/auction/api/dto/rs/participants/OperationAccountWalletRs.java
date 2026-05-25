package ws.academy.auction.api.dto.rs.participants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.transactions.TransactionRs;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO детальной информации об операции")
public class OperationAccountWalletRs {
    private UUID participant;
    private Double amount;
    private List<TransactionRs> transactions;
}
