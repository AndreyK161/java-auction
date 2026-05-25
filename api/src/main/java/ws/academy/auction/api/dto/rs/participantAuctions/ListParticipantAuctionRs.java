package ws.academy.auction.api.dto.rs.participantAuctions;

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
@Schema(description = "DTO для списка участников аукциона")
public class ListParticipantAuctionRs {

    @Schema(description = "Список участников аукциона")
    private List<ParticipantAuctionItem> items;

    @Schema(description = "Данные постраничной навигации")
    private ListPageData page;
}
