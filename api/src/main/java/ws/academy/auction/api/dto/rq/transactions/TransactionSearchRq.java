package ws.academy.auction.api.dto.rq.transactions;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSearchRq {

    @Schema(description = "Номер страницы полученного списка лотов")
    @Min(value = 1, message = "Page number must be greater than or equal to 1")
    private Integer page = 1;

    @Schema(description = "Количество записей на странице полученного списка лотов")
    @Min(value = 30, message = "Count per page must be greater than or equal to 30")
    private Integer count = 30;

    @Schema(description = "Дата транзакции с")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateFrom;

    @Schema(description = "Дата транзакции по")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateTo;

    @Schema(description = "Идентификатор участника, перечень транзакций которого будут показан")
    private UUID guid;
}
