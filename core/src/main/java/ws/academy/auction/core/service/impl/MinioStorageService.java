package ws.academy.auction.core.service.impl;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.academy.auction.core.exception.StorageFileUploadException;
import ws.academy.auction.core.exception.NotFoundException;
import ws.academy.auction.core.service.FileStorageService;

import java.util.UUID;

@Service
public class MinioStorageService implements FileStorageService {
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public MinioStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String upload(String folder, UUID fileId, MultipartFile file, String contentType) {
        String objectName = folder + "/" + fileId;
        try {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(contentType)
                    .build();
            minioClient.putObject(args);
            return objectName;
        } catch (Exception e) {
            throw new StorageFileUploadException("Ошибка загрузки файла: " + e.getMessage(), e);
        }
    }

    @Override
    public String generatePresignedUrl(String objectPath) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectPath)
                            .expiry(3600)
                            .build()
            );
        } catch (Exception e) {
            throw new NotFoundException("Ошибка получения ссылки: " + e.getMessage(), e);
        }
    }
}
