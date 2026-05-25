package ws.academy.auction.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ws.academy.auction.api.controller.FileController;
import ws.academy.auction.api.dto.rs.photos.ImageRef;
import ws.academy.auction.core.exception.StorageFileUploadException;
import ws.academy.auction.core.service.PhotoService;

@RestController
@RequiredArgsConstructor
public class FileControllerImpl implements FileController {

    private final PhotoService photoService;

    @Override
    public ImageRef uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return photoService.addImageFile(file);
        } catch (Exception e) {
            throw new StorageFileUploadException(e.getMessage(), e);
        }
    }
}
