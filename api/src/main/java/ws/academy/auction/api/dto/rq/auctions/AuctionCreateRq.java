package ws.academy.auction.api.dto.rq.auctions;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для создания нового аукциона")
public class AuctionCreateRq {

    @Schema(description = "Дата начала аукциона")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;
}
