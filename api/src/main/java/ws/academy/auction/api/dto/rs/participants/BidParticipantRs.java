package ws.academy.auction.api.dto.rs.participants;

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
@Schema(description = "Участник, сделавший ставку")
public class BidParticipantRs {

    private UUID guid;
    private String email;
    private String fullName;
    private Long balance;
}
