package ws.academy.auction.api.dto.rs.auctions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Статус торгов")
public class AuctionStatusDto {
    @Schema(description = "Код статуса")
    private String code;

    @Schema(description = "Название статуса")
    private String name;
}
