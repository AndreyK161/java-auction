package ws.academy.auction.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.academy.auction.api.dto.rs.photos.ImageRef;
import ws.academy.auction.core.entity.Photo;
import ws.academy.auction.core.mapper.PhotoMapper;
import ws.academy.auction.core.repository.PhotoRepository;
import ws.academy.auction.core.service.FileStorageService;
import ws.academy.auction.core.service.PhotoService;
import ws.academy.auction.core.validators.Validator;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final Validator<MultipartFile> imageValidator;
    private final FileStorageService fileStorageService;

    @Override
    public ImageRef addImageFile(MultipartFile file) {
        imageValidator.validate(file);

        String mimeType = file.getContentType();
        Photo photo = saveInitialPhoto(mimeType);

        String objectName = fileStorageService.upload("images", photo.getGuid(), file, mimeType);
        String url = fileStorageService.generatePresignedUrl(objectName);

        photo.setPath("/" + objectName);
        photoRepository.save(photo);

        return mapToImageRef(photo, url);
    }

    private Photo saveInitialPhoto(String mimeType) {
        Photo photo = new Photo();
        photo.setMimeType(mimeType);
        return photoRepository.save(photo);
    }

    private ImageRef mapToImageRef(Photo photo, String url) {
        ImageRef imageRef = photoMapper.buildImageRef(photo);
        imageRef.setUrl(url);
        return imageRef;
    }
}
