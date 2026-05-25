package ws.academy.auction.core.helpers;

import ws.academy.auction.api.dto.rs.photos.PhotoDetails;
import ws.academy.auction.core.entity.Lot;

import java.util.List;

public interface PhotoHelper {
    List<PhotoDetails> getPhotoDetailsList(Lot lot);
}
