package ws.academy.auction.core.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.academy.auction.api.dto.rs.photos.ImageRef;

/**
 * Сервис по управлению изображениями лотов
 */

@Service
public interface PhotoService {
    /**
     * Добавление изображения, передаваемое клиентом
     *
     * @param file объект - изображение, передаваемое клиентом
     * @return ImageRef объект - является обычным dto объектом для ответа при получении 201 статуса
     */
    ImageRef addImageFile(MultipartFile file);
}
