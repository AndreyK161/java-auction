package ws.academy.auction.core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ws.academy.auction.core.entity.Auction;
import ws.academy.auction.core.entity.AuctionLot;
import ws.academy.auction.core.entity.Lot;

@Mapper
public interface AuctionLotMapper {
    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "auction", source = "auction")
    @Mapping(target = "lot", source = "lot")
    AuctionLot buildAuctionLot(Auction auction, Lot lot);
}
