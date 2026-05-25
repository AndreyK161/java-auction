package ws.academy.auction.api.dto.rs.bids;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация по статусу ставки")
public class BidStatusRs {
    private String status;
    private UUID guid;
}
