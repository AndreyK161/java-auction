package ws.academy.auction.core.validators;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ws.academy.auction.core.exception.StorageFileUploadException;

import java.util.Set;

@Component
public class ImageValidator implements Validator<MultipartFile> {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png");

    @Override
    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new StorageFileUploadException("Файл не должен быть пустым");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new StorageFileUploadException("Тип файла не поддерживается: " + contentType);
        }
    }
}
