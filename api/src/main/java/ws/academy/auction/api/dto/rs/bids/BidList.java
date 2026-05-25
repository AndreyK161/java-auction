package ws.academy.auction.api.dto.rs.bids;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ws.academy.auction.api.dto.rq.bids.BidRq;
import ws.academy.auction.api.dto.rs.auctions.CurrentAuctionRs;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO списка ставок по лоту")
public class BidList {

    @Schema(description = "Аукцион в котором принимает участие лот")
    private CurrentAuctionRs trade;

    @Schema(description = "Список транзакций")
    private List<BidRq> items;
}
