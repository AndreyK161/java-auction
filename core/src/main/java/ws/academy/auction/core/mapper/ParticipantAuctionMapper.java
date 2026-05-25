package ws.academy.auction.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ws.academy.auction.api.dto.rs.participants.ParticipantAuctionDetails;
import ws.academy.auction.core.entity.ParticipantAuction;

/**
 * Маппер для сущности ParticipantAuction
 */
@Mapper
public interface ParticipantAuctionMapper {

    /**
     * Получение dto-объекта сущности ParticipantAuction
     *
     * @param participantAuction объект - передаваемая сущность для маппинга в dto
     * @return ParticipantAuctionDetails объект - dto-объект сущности ParticipantAuction
     */
    @Mapping(source = "participantNumber", target = "number")
    ParticipantAuctionDetails toDetails(ParticipantAuction participantAuction);
}
