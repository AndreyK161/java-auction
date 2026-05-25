package ws.academy.auction.api.dto.rq.auctions;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AuctionSearchRequest {
    @Schema(description = "Период проведения от")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate from;

    @Schema(description = "Период проведения до")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate to;

    @Schema(description = "Перечень необходимых статусов")
    private List<String> statuses;

    @Schema(description = "Номер страницы полученного списка лотов")
    @Min(value = 1, message = "Page number must be greater than or equal to 1")
    private Integer page = 1;

    @Schema(description = "Количество записей на странице полученного списка лотов")
    @Min(value = 1, message = "Count per page must be greater than or equal to 1")
    private Integer count = 30;

    @Schema(description = "Перечень участников аукционов")
    private List<UUID> participants;
}
