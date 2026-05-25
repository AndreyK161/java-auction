package ws.academy.auction.api.dto.rq.participants;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ParticipantSearchRq {
    @Schema(description = "Номер страницы полученного списка лотов")
    @Min(value = 1, message = "Page number must be greater than or equal to 1")
    private Integer page = 1;

    @Schema(description = "Количество записей на странице полученного списка лотов")
    @Min(value = 1, message = "Count per page must be greater than or equal to 1")
    private Integer count = 30;

    @Pattern(regexp = "asc|desc", message = "SortBy must be 'asc' or 'desc'")
    private String by = "asc";

    @Pattern(regexp = "number|fullName", message = "SortBy must be 'number' or 'fullName'")
    private String sort;

    @Schema(description = "Глобальный идентификатор сущности")
    @NotNull
    private UUID guid;
}
