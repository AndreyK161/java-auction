package ws.academy.auction.api.dto.rq.lots;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO объект-запрос для отправки guid лота")
public class LotGuidRq {
    private UUID lot;
}
