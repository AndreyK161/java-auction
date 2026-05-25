package ws.academy.auction.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ws.academy.auction.api.dto.rs.participants.OperationAccountWalletRs;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;
import ws.academy.auction.core.entity.Participant;
import ws.academy.auction.core.entity.User;

/**
 * Маппер для сущности Participant
 */
@Mapper
public interface ParticipantMapper {
    /**
     * Получение dto-объекта сущности Participant
     *
     * @param participant объект - передаваемая сущность для маппинга в dto
     * @return ParticipantDetails объект - dto-объект сущности Participant
     */
    ParticipantDetails buildParticipantDetails(Participant participant);

    /**
     * Получение сущности Participant
     *
     * @param user объект - передаваемая сущность для маппинга в сущность
     * @return Participant объект - объект-сущность Participant
     */
    @Mapping(source = "username", target = "fullName")
    Participant buildParticipant(User user);

    @Mapping(source = "guid", target = "participant")
    @Mapping(source = "balance", target = "amount")
    OperationAccountWalletRs buildOperationWallet(Participant participant);
}
