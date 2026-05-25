package ws.academy.auction.api.dto.rq.lots;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LotSearchRequest {

    @Schema(description = "Номер страницы полученного списка лотов")
    @Min(value = 1, message = "Page number must be greater than or equal to 1")
    private Integer page = 1;

    @Schema(description = "Количество записей на странице полученного списка лотов")
    @Min(value = 30, message = "Count per page must be greater than or equal to 30")
    private Integer count = 30;

    @Pattern(regexp = "asc|desc", message = "SortBy must be 'asc' or 'desc'")
    private String by = "asc";

    @Schema(description = "Фильтр начала диапазона стоимости списка лотов")
    private BigDecimal priceFrom;

    @Schema(description = "Фильтр конца диапазона стоимости списка лотов")
    private BigDecimal priceTo;

    @Schema(description = "Фильтр имени лота списка лотов")
    private String name;

    @Schema(description = "Фильтр удаленных лотов")
    private String showDeleted = "N";
}
