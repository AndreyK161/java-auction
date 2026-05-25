package ws.academy.auction.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ws.academy.auction.api.controller.LotController;
import ws.academy.auction.api.dto.rq.lots.CreateLotRq;
import ws.academy.auction.api.dto.rq.lots.LotSearchRequest;
import ws.academy.auction.api.dto.rq.lots.UpdateLotRq;
import ws.academy.auction.api.dto.rs.lots.LotDetails;
import ws.academy.auction.api.dto.rs.lots.LotList;
import ws.academy.auction.core.service.LotService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LotControllerImpl implements LotController {

    private final LotService lotService;

    @Override
    public LotDetails createLot(CreateLotRq createLotRq) {
        return lotService.createLot(createLotRq);
    }

    @Override
    public LotList getLots(LotSearchRequest request) {
        return lotService.getLots(request);
    }

    @Override
    public LotDetails getLotById(UUID guid) {
        return lotService.getLotById(guid);
    }

    @Override
    public LotDetails updateLot(UpdateLotRq updateLotRq, UUID guid) {
        return lotService.updateLot(updateLotRq, guid);
    }

    @Override
    public void deleteLot(UUID guid) {
        lotService.deleteLot(guid);
    }
}
