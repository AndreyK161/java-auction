package ws.academy.auction.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ws.academy.auction.api.dto.AuctionStatus;
import ws.academy.auction.api.dto.rs.auctions.AuctionListItem;
import ws.academy.auction.api.dto.rs.auctions.AuctionRs;
import ws.academy.auction.api.dto.rs.auctions.AuctionStatusDto;
import ws.academy.auction.api.dto.rs.auctions.AuctionSubmittingRs;
import ws.academy.auction.core.entity.Auction;

/**
 * Маппер для сущности Auction
 */
@Mapper
public interface AuctionMapper {
    /**
     * Получение dto-объекта сущности Auction
     * @param auction объект - передаваемая сущность для маппинга в dto
     * @return AuctionRs объект - dto-объект сущности Auction
     */
    @Mapping(source = "guid", target = "id")
    AuctionRs buildAuctionRs(Auction auction);

    /**
     * Получение dto-объекта перечисления сущности Auction
     * @param status объект - передаваемая сущность-перечисление для маппинга в dto
     * @return AuctionStatusDto объект - dto-объект сущности перечисления Auction
     */
    default AuctionStatusDto mapStatus(AuctionStatus status) {
        return new AuctionStatusDto(status.name(), status.getCode());
    }

    /**
     * Получение dto-объекта сущности Auction
     *
     * @param auction объект - передаваемая сущность для маппинга в dto
     * @return AuctionSubmittingRs объект - dto-объект сущности Auction
     */
    @Mapping(source = "guid", target = "id")
    AuctionSubmittingRs buildAuctionSubmittingRs(Auction auction);

    /**
     * Получение dto-объекта сущности Auction
     *
     * @param auction объект - передаваемая сущность для маппинга в dto
     * @return AuctionListItem объект - dto-объект сущности Auction
     */
    AuctionListItem toAuctionListItem(Auction auction);
}
