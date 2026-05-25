package ws.academy.auction.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ws.academy.auction.api.dto.rq.users.CreateUserRq;
import ws.academy.auction.api.dto.rq.users.UpdateUserRq;
import ws.academy.auction.api.dto.rq.users.UserSearchRequest;
import ws.academy.auction.api.dto.rs.GuidRs;
import ws.academy.auction.api.dto.rs.users.UserList;
import ws.academy.auction.api.dto.rs.users.UserDetails;

import java.util.UUID;

@Validated
@RequestMapping("/api/v1/users")
@Tag(name = "UserController", description = "Контроллер для работы с пользователями")
public interface UserController {

    @Operation(summary = "Добавление пользователя")
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    GuidRs addUser(@RequestBody @Valid CreateUserRq request);

    @Operation(summary = "Получение детальной информации по пользователю")
    @GetMapping("/{guid}/")
    UserDetails getUserDetails(@PathVariable UUID guid);

    @Operation(summary = "Получение списка пользователей")
    @GetMapping("/")
    UserList getUserListDetails(@Valid @ModelAttribute UserSearchRequest request);

    @Operation(summary = "Обновление данных пользователя")
    @PutMapping("/{guid}/")
    void updateUser(@PathVariable UUID guid, @RequestBody @Valid UpdateUserRq request);

    @Operation(summary = "Удаление пользователя по идентификатору")
    @DeleteMapping("/{guid}/")
    void deleteUser(@PathVariable UUID guid);

}
