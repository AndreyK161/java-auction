package ws.academy.auction.core.helpers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ws.academy.auction.api.dto.rs.photos.PhotoDetails;
import ws.academy.auction.core.entity.Lot;
import ws.academy.auction.core.entity.Photo;
import ws.academy.auction.core.helpers.PhotoHelper;
import ws.academy.auction.core.mapper.PhotoMapper;
import ws.academy.auction.core.repository.PhotoRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PhotoHelperImpl implements PhotoHelper {
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    @Override
    public List<PhotoDetails> getPhotoDetailsList(Lot lot) {
        List<Photo> photos = photoRepository.findAllByEntityTypeAndEntityGuid("LOT", lot.getGuid());
        return photos.stream()
                .map(photoMapper::buildPhotoDetails)
                .toList();
    }
}
