package ws.academy.auction.core.service;

import org.springframework.stereotype.Service;
import ws.academy.auction.api.dto.rq.participantAuctions.ParticipantAuctionSearchRq;
import ws.academy.auction.api.dto.rq.participants.ParticipantSearchRq;
import ws.academy.auction.api.dto.rq.participants.SubmittingRq;
import ws.academy.auction.api.dto.rs.GuidRs;
import ws.academy.auction.api.dto.rs.participantAuctions.ListParticipantAuctionRs;
import ws.academy.auction.api.dto.rs.participants.ParticipantAuctionDetails;
import ws.academy.auction.api.dto.rs.participants.ParticipantListRs;

import java.util.UUID;

@Service
public interface ParticipantAuctionService {
    /**
     * Подача заявки на участие аукциона + бонусом добавление аукциона
     *
     * @param guid идентификатор аукциона
     * @param request идентификатор участника, идентификаторы лотов
     * @return GuidRs объект - объект-dto возвращаемая UUID сущности
     */
    GuidRs submittingAnAuction(UUID guid, SubmittingRq request);

    /**
     * Подача заявки на участие аукцион
     *
     * @param guid идентификатор аукциона
     */
    void  submittingAnAuction(UUID guid);

    /**
     * Получение перечня участников, которые участвуют в конкретных торгах
     *
     * @param auctionGuid идентификатор аукциона
     * @param request - запрос передаваемый клиентом с нужными фильтрами
     * @return ParticipantListRs объект - dto-объект списка участников, которые участвуют в конкретных лотах
     */
    ParticipantListRs getParticipants(UUID auctionGuid, ParticipantSearchRq request);

    /**
     * Получение детальной информации о найденном участнике аукциона
     *
     * @param guid идентификатор аукциона
     * @param participantGuid идентификатор участника
     * @return ParticipantAuctionDetails объект - dto-объект сущности ParticipantAuction
     */
    ParticipantAuctionDetails getParticipantAuction(UUID guid, UUID participantGuid);

    /**
     * Удаление участника аукциона
     *
     * @param guid идентификатор аукциона
     * @param participantGuid идентификатор участника
     */
    void deleteParticipantAuction(UUID guid, UUID participantGuid);

    /**
     * Действие над заявкой участника аукциона [admin]
     *
     * @param guid идентификатор аукциона
     * @param action действие
     * @param lotGuid идентификатор лота
     */
    void actionSubmittingAnAuction(UUID guid, String action, UUID lotGuid);

    /**
     * Получение перечня участников аукциона
     *
     * @param guid идентификатор аукциона
     * @param request запрос передаваемый клиентом
     * @return ListParticipantAuctionRs список объектов - список объектов-dto сущности ParticipantAuctions
     */
    ListParticipantAuctionRs getParticipantAuctions(UUID guid, ParticipantAuctionSearchRq request);

    ListParticipantAuctionRs getAllParticipantAuctions(ParticipantAuctionSearchRq request);
}
