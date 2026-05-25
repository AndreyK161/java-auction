package ws.academy.auction.api.dto.rs.photos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Информация о загруженной фотографии")
public class ImageRef {

    @Schema(description = "Идентификатор фото")
    private UUID guid;

    @Schema(description = "Тип изображения")
    private String mimeType;

    @Schema(description = "URL-адрес изображения")
    private String url;
}
