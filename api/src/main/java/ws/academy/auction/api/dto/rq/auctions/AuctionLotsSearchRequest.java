package ws.academy.auction.api.dto.rq.auctions;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AuctionLotsSearchRequest {
    @Schema(description = "Номер страницы полученного списка лотов")
    @Min(value = 1, message = "Page number must be greater than or equal to 1")
    private Integer page;

    @Schema(description = "Количество записей на странице полученного списка лотов")
    @Min(value = 30, message = "Count per page must be greater than or equal to 30")
    private Integer count;

    @Pattern(regexp = "asc|desc", message = "SortBy must be 'asc' or 'desc'")
    private String by;

    @Pattern(regexp = "number|title|minPrice", message = "SortBy must be 'number' or 'title' or 'minPrice'")
    private String sort;

    @Schema(description = "Фильтр статуса лотов")
    private String status;

    @Schema(description = "Идентификатор аукциона")
    private UUID auctionGuid;

}
