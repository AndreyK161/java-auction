package ws.academy.auction.core.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Сервис по управлению файлами в хранилище
 */
@Service
public interface FileStorageService {
    /**
     * Загрузка файла в хранилище
     *
     * @param folder - директория где будет храниться файл
     * @param fileId - идентификатор файла
     * @param file - файл
     * @param contentType - тип файла
     * @return String - путь файла
     */
    String upload(String folder, UUID fileId, MultipartFile file, String contentType);

    /**
     * Создание URL файла в хранилище для передачи в ответ запроса
     *
     * @param objectPath - путь файла
     * @return String - сгенерированный URL файла в хранилище
     */
    String generatePresignedUrl(String objectPath);
}
