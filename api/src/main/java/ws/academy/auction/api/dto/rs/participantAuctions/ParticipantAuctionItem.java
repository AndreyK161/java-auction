package ws.academy.auction.api.dto.rs.participantAuctions;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ws.academy.auction.api.dto.rq.lots.LotGuidRq;
import ws.academy.auction.api.dto.rs.auctions.AuctionSubmittingRs;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для списка участников аукциона")
public class ParticipantAuctionItem {

    @Schema(description = "Дата и время создания заявки")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime createAt;

    @Schema(description = "DTO участника")
    private ParticipantDetails participant;

    @Schema(description = "DTO аукциона на который подал участник заявку")
    private AuctionSubmittingRs auction;

    @Schema(description = "Список идентификаторов лотов")
    private List<LotGuidRq> lots;

    @Schema(description = "Список возможных действий")
    private List<String> actions;
}
