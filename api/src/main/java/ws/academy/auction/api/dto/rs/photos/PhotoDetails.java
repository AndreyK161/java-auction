package ws.academy.auction.api.dto.rs.photos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "DTO с детальной информацией изображения лота")
public class PhotoDetails {

    @Schema(description = "Идентификатор изображения лота")
    private UUID guid;

    @Schema(description = "Тип изображения лота")
    private String mimeType;

    @Schema(description = "Путь до изображения лота")
    private String path;

    @Schema(description = "URL изображения лота")
    private String url;
}
