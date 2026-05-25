package ws.academy.auction.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ws.academy.auction.api.controller.UserAccessController;
import ws.academy.auction.api.dto.rq.users.*;
import ws.academy.auction.api.dto.rs.users.ResultMessageRs;
import ws.academy.auction.api.dto.rs.users.JwtRs;
import ws.academy.auction.core.service.PasswordRecoveryService;
import ws.academy.auction.core.service.UserAccessService;

@RestController
@RequiredArgsConstructor
public class UserAccessControllerImpl implements UserAccessController {

    private final UserAccessService userAccessService;
    private final PasswordRecoveryService passwordRecoveryService;

    @Override
    public JwtRs loginUser(AuthenticateUserRq request) {
        return userAccessService.loginUser(request);
    }

    @Override
    public JwtRs refreshTokenUser(RefreshTokenRq request) {
        return userAccessService.refreshTokenUser(request);
    }

    @Override
    public ResultMessageRs passwordRecoveryUser(EmailRq request) {
        return passwordRecoveryService.sendPasswordRecoveryLink(request);
    }

    @Override
    public ResultMessageRs passwordResetUser(ResetPasswordRq request) {
        return passwordRecoveryService.resetPassword(request);
    }

    @Override
    public ResultMessageRs registerUser(RegisterUserRq request) {
        return userAccessService.registerUser(request);
    }

    @Override
    public ResultMessageRs activateUser(ActivateUserRq request) {
        return userAccessService.activateUser(request);
    }
}
