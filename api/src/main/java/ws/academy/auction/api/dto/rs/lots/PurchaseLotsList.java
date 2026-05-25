package ws.academy.auction.api.dto.rs.lots;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rs.ListPageData;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для списка выигранных лотов участником")
public class PurchaseLotsList {
    private List<PurchaseLotItem> items;
    private ListPageData page;
}
