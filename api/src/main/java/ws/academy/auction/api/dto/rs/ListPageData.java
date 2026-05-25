package ws.academy.auction.api.dto.rs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Данные постраничной навигации")
public class ListPageData {

    @Schema(description = "Номер страницы")
    private Integer page;

    @Schema(description = "Количество элементов на странице")
    private Integer size;

    @Schema(description = "Количество страниц")
    private Integer totalPages;
}
