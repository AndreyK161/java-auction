package ws.academy.auction.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ws.academy.auction.api.dto.rq.users.FullNameRq;
import ws.academy.auction.api.dto.rs.users.UserDetails;

@Validated
@RequestMapping("/api/v1/auth/current-user/")
@Tag(name = "UserController", description = "Контроллер для просмотра текущих авторизованных пользователей в системе")
public interface CurrentUserController {

    @Operation(summary = "Получение текущего авторизованного пользователя")
    @GetMapping
    UserDetails getCurrentUser();

    @Operation(summary = "Обновление данных профиля пользователя")
    @PutMapping
    void updateCurrentUser(@RequestBody FullNameRq request);
}
