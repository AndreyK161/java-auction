package ws.academy.auction.api.dto.rs.auctions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionShortRs {

    @Schema(description = "Идентификатор торгов")
    private UUID guid;
}
