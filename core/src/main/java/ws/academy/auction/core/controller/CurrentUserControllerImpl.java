package ws.academy.auction.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ws.academy.auction.api.controller.CurrentUserController;
import ws.academy.auction.api.dto.rq.users.FullNameRq;
import ws.academy.auction.api.dto.rs.users.UserDetails;
import ws.academy.auction.core.service.CurrentUserService;

@RestController
@RequiredArgsConstructor
public class CurrentUserControllerImpl implements CurrentUserController {

    private final CurrentUserService currentUserService;

    @Override
    public UserDetails getCurrentUser() {
        return currentUserService.showCurrentUser();
    }

    @Override
    public void updateCurrentUser(FullNameRq request) {
        currentUserService.updateCurrentUser(request);
    }
}
