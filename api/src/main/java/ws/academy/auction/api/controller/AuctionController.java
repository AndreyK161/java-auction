package ws.academy.auction.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ws.academy.auction.api.dto.Bidding;
import ws.academy.auction.api.dto.BiddingBoard;
import ws.academy.auction.api.dto.LotBid;
import ws.academy.auction.api.dto.rq.auctions.AuctionCreateRq;
import ws.academy.auction.api.dto.rq.auctions.AuctionLotsSearchRequest;
import ws.academy.auction.api.dto.rq.auctions.AuctionSearchRequest;
import ws.academy.auction.api.dto.rq.bids.CreateBidRq;
import ws.academy.auction.api.dto.rq.lots.LotGuidRq;
import ws.academy.auction.api.dto.rs.auctions.AuctionList;
import ws.academy.auction.api.dto.rs.auctions.AuctionLotList;
import ws.academy.auction.api.dto.rs.bids.BidList;
import ws.academy.auction.api.dto.rs.bids.BidStatusRs;
import ws.academy.auction.api.dto.rs.participantAuctions.ListParticipantAuctionRs;
import ws.academy.auction.api.dto.rq.participantAuctions.ParticipantAuctionSearchRq;
import ws.academy.auction.api.dto.rq.participants.ParticipantSearchRq;
import ws.academy.auction.api.dto.rq.participants.SubmittingRq;
import ws.academy.auction.api.dto.rs.GuidRs;
import ws.academy.auction.api.dto.rs.auctions.AuctionRs;
import ws.academy.auction.api.dto.rs.participants.ParticipantAuctionDetails;
import ws.academy.auction.api.dto.rs.participants.ParticipantListRs;

import java.util.UUID;

@Validated
@RequestMapping("/api/v1/auctions/")
@Tag(name = "AuctionController", description = "Контроллер для работы с торгами")
public interface AuctionController {

    @Operation(summary = "Создание торгов")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    GuidRs createAuction(@Valid @RequestBody AuctionCreateRq startDate);

    @Operation(summary = "Обновление торгов")
    @PatchMapping("{guid}/")
    void updateAuction(@Valid @RequestBody AuctionCreateRq startDate, @PathVariable UUID guid);

    @Operation(summary = "Удаление торгов")
    @DeleteMapping("{guid}/")
    void deleteAuction(@PathVariable UUID guid);

    @Operation(summary = "Получение детальной информации по аукциону")
    @GetMapping("{guid}/")
    AuctionRs getAuctionWithId(@PathVariable UUID guid);

    @Operation(summary = "Запуск/Остановка торгов из перечня опубликованных/запущенных")
    @PostMapping("/{guid}/{action}/")
    void actionAuction(@PathVariable UUID guid, @PathVariable String action);

    @Operation(summary = "Получение перечня лотов, участвующих в торгах")
    @GetMapping("/{guid}/lots/")
    AuctionLotList getAuctionLots(@PathVariable UUID guid, @Valid @ModelAttribute AuctionLotsSearchRequest request);

    @Operation(summary = "Регистрация лота на торгах")
    @PostMapping("/{guid}/lots/")
    void addAuctionLot(@PathVariable UUID guid, @Valid @RequestBody LotGuidRq lotGuid);

    @Operation(summary = "Регистрация лота на торгах [admin]")
    @PutMapping("/{guid}/lots/{lotGuid}/")
    void addAuctionLotAdmin(@PathVariable UUID guid, @PathVariable UUID lotGuid);

    @Operation(summary = "Удаление лота с торгов")
    @DeleteMapping("/{guid}/lots/")
    void deleteAuctionLot(@PathVariable UUID guid, @Valid @RequestBody LotGuidRq lotGuid);

    @Operation(summary = "Удаление лота с торгов [admin]")
    @DeleteMapping("/{guid}/lots/{lotGuid}/")
    void deleteAuctionLotAdmin(@PathVariable UUID guid, @PathVariable UUID lotGuid);

    @Operation(summary = "Получение списка ставок по лоту")
    @GetMapping("/{id}/lot/{lotId}/bid")
    Bidding getBids(@PathVariable Integer id, @PathVariable Integer lotId);

    @Operation(summary = "Добавление ставки на лот")
    @PostMapping("/{id}/lot/{lotId}/bid")
    Bidding placeBid(@PathVariable Integer id, @PathVariable Integer lotId, @RequestBody @Valid LotBid lotBid);

    @Operation(summary = "Подача заявки на торги, дополнительно можно выставить лоты")
    @PostMapping("/{guid}/participants/")
    GuidRs submittingAnAuction(@PathVariable UUID guid, @RequestBody SubmittingRq request);

    @Operation(summary = "Перечень участников, которые участвуют в конкретных торгах")
    @GetMapping("/{guid}/participants/")
    ParticipantListRs getParticipants(@PathVariable UUID guid, ParticipantSearchRq request);

    @Operation(summary = "Детальная информация об участнике аукциона")
    @GetMapping("/{guid}/participants/{participantGuid}/")
    ParticipantAuctionDetails getParticipantAuctionDetails(@PathVariable UUID guid, @PathVariable UUID participantGuid);

    @Operation(summary = "Удаление участника с аукциона, при этом его номер остается в свободном доступе")
    @DeleteMapping("/{guid}/participants/{participantGuid}/")
    void deleteParticipantAuctionDetails(@PathVariable UUID guid, @PathVariable UUID participantGuid);

    @Operation(summary = "Подача заявки участия на аукцион")
    @PostMapping("/{guid}/participants/participate-application/")
    void submittingAnAuction(@PathVariable UUID guid);

    @Operation(summary = "Действие над заявкой участия в аукционе")
    @PostMapping("/{guid}/participants/participate-application/{action}/")
    void actionSubmittingAnAuction(@PathVariable UUID guid, @PathVariable String action,
                                   @RequestBody @Valid UUID guidParticipant);

    @Operation(summary = "Выставить лот на продажу активного аукциона")
    @PostMapping("/{guid}/lots/{lotGuid}/for-sale/")
    void setSellerLot(@PathVariable UUID guid, @PathVariable UUID lotGuid);

    @Operation(summary = "Перечень всех заявок на участие (admin)")
    @GetMapping("/participants/participate-application/")
    ListParticipantAuctionRs getAllParticipantAuctions(@Valid @ModelAttribute ParticipantAuctionSearchRq request);

    @Operation(summary = "Перечень заявок на участие по аукциону")
    @GetMapping("/{guid}/participants/participate-application/")
    ListParticipantAuctionRs getParticipantAuctions(@PathVariable UUID guid,
                                                    @Valid @ModelAttribute ParticipantAuctionSearchRq request);

    @Operation(summary = "Список торгов")
    @GetMapping
    AuctionList getAuctions(@Valid @ModelAttribute AuctionSearchRequest request);

    @Operation(summary = "Добавление ставки на лот")
    @PostMapping("{auctionId}/lots/{lotId}/bids/")
    BidStatusRs addBidLot(@PathVariable UUID auctionId, @PathVariable UUID lotId, @RequestBody CreateBidRq request);

    @Operation(summary = "Список ставок по лоту")
    @GetMapping("{auctionId}/lots/{lotId}/bids/")
    BidList getBids(@PathVariable UUID auctionId, @PathVariable UUID lotId);

    @Operation(summary = "Получить \"табло\" по аукциону")
    @GetMapping("/{id}/bidding/board")
    BiddingBoard getBiddingBoard(@PathVariable Integer id);

}
