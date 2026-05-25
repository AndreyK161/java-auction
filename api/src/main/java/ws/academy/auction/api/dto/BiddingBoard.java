package ws.academy.auction.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO, содержащее данные для отображения \"табло\" по торгам")
public class BiddingBoard {

    @Schema(description = "Идентификатор торгов")
    private Integer auctionId;

    @Schema(description = "Данные о торгующихся лотах")
    private List<Bidding> biddingList;
}
