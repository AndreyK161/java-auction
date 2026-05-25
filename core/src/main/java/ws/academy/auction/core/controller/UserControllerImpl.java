package ws.academy.auction.core.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ws.academy.auction.api.controller.UserController;
import ws.academy.auction.api.dto.rq.users.CreateUserRq;
import ws.academy.auction.api.dto.rq.users.UpdateUserRq;
import ws.academy.auction.api.dto.rq.users.UserSearchRequest;
import ws.academy.auction.api.dto.rs.GuidRs;
import ws.academy.auction.api.dto.rs.users.UserDetails;
import ws.academy.auction.api.dto.rs.users.UserList;
import ws.academy.auction.core.service.UserService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public GuidRs addUser(CreateUserRq request) {
        return userService.addUser(request);
    }

    @Override
    public UserDetails getUserDetails(UUID guid) {
        return userService.findByGuid(guid);
    }

    @Override
    public UserList getUserListDetails(UserSearchRequest request) {
        return userService.getUsers(request);
    }

    @Override
    public void updateUser(UUID guid, UpdateUserRq request) {
        userService.updateUserByGuid(guid, request);
    }

    @Override
    public void deleteUser(UUID guid) {
        userService.deleteUserByGuid(guid);
    }
}
