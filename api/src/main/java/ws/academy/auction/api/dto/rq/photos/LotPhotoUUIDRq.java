package ws.academy.auction.api.dto.rq.photos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO для схемы GuidRef в спецификации")
public class LotPhotoUUIDRq {

    @NotNull
    private UUID guid;
}
