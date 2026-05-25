package ws.academy.auction.api.dto.rq.participants;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantUUIDRq {

    @NotNull
    @Schema(description = "Идентификатор продавца лота")
    private UUID guid;
}
