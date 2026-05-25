package ws.academy.auction.api.dto.rq.participants;

import lombok.*;
import ws.academy.auction.api.dto.rq.lots.LotGuidRq;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubmittingRq {
    private UUID participant;
    private List<LotGuidRq> lots;
}
