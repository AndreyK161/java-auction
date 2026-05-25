package ws.academy.auction.core.mapper;

import org.mapstruct.Mapper;
import ws.academy.auction.api.dto.rs.photos.ImageRef;
import ws.academy.auction.api.dto.rs.photos.PhotoDetails;
import ws.academy.auction.core.entity.Photo;

/**
 * Маппер для сущности Photo
 */
@Mapper
public interface PhotoMapper {

    /**
     * Получение dto-объекта сущности Photo
     *
     * @param lotPhoto объект - передаваемая сущность для маппинга в dto
     * @return ImageRef объект - dto-объект сущности Photo
     */
    ImageRef buildImageRef(Photo lotPhoto);

    /**
     * Получение dto-объекта сущности Photo
     *
     * @param photo объект - передаваемая сущность для маппинга в dto
     * @return PhotoDetails объект - dto-объект сущности Photo
     */
    PhotoDetails buildPhotoDetails(Photo photo);
}
