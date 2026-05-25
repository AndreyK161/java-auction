package ws.academy.auction.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ws.academy.auction.api.dto.rq.lots.CreateLotRq;
import ws.academy.auction.api.dto.rq.lots.LotSearchRequest;
import ws.academy.auction.api.dto.rq.lots.UpdateLotRq;
import ws.academy.auction.api.dto.rs.lots.LotDetails;
import ws.academy.auction.api.dto.rs.lots.LotList;

import java.util.UUID;

@Validated
@RequestMapping("/api/v1/lots/")
@Tag(name = "LotController", description = "Контроллер для работы с лотами")
public interface LotController {

    @Operation(summary = "Создание нового лота")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    LotDetails createLot(@RequestBody @Valid CreateLotRq createLotRq);

    @Operation(summary = "Получение списка лотов с постраничной навигацией")
    @GetMapping()
    LotList getLots(@Valid @ModelAttribute LotSearchRequest request);

    @Operation(summary = "Получение лота по идентификатору")
    @GetMapping("{guid}/")
    LotDetails getLotById(@PathVariable("guid") UUID guid);

    @Operation(summary = "Обновление лота по идентификатору")
    @PutMapping("{guid}/")
    LotDetails updateLot(@RequestBody @Valid UpdateLotRq updateLotRq, @PathVariable("guid") UUID guid);

    @Operation(summary = "Удаление лота по идентификатору")
    @DeleteMapping("{guid}/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteLot(@PathVariable("guid") UUID guid);
}
