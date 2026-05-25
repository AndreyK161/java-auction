package ws.academy.auction.api.dto.rq.participants;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO детальной информации об банковской карте")
public class CardRq {
    private String number;
    private String expire;
    private String cvv;
}
