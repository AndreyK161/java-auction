package ws.academy.auction.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ws.academy.auction.api.dto.rq.users.*;
import ws.academy.auction.api.dto.rs.users.ResultMessageRs;
import ws.academy.auction.api.dto.rs.users.JwtRs;

@Validated
@RequestMapping("/api/v1/auth")
@Tag(name = "UserAccessController", description = "Контроллер для работы с доступом пользователей в систему")
public interface UserAccessController {

    @Operation(summary = "Аутентификация пользователя. Получение токена доступа и токена обновления")
    @PostMapping("/login/")
    JwtRs loginUser(@RequestBody @Valid AuthenticateUserRq request);

    @Operation(summary = "Обновление токена пользователя")
    @PostMapping("/refresh-token/")
    JwtRs refreshTokenUser(@RequestBody @Valid RefreshTokenRq request);

    @Operation(summary = "Восстановление пароля пользователя. " +
            "В результате пользователю отправляется email со ссылкой на сброс пароля")
    @PostMapping("/password-recovery/")
    ResultMessageRs passwordRecoveryUser(@RequestBody @Valid EmailRq request);

    @Operation(summary = "Сброс пароля пользователя, по ссылке, пришедшей из письма")
    @PostMapping("/reset-password/")
    ResultMessageRs passwordResetUser(@RequestBody @Valid ResetPasswordRq request);

    @Operation(summary = "Регистрация пользователя." +
            " При отправке запроса, пользователю должна прийти одноразовая ссылка для активации")
    @PostMapping("/register/")
    ResultMessageRs registerUser(@RequestBody @Valid RegisterUserRq request);

    @Operation(summary = "Активация аккаунта пользователя, полученного при вводе регистрационных данных")
    @PostMapping("/activate/")
    ResultMessageRs activateUser(@RequestBody @Valid ActivateUserRq request);

}
