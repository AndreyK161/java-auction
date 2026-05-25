package ws.academy.auction.core.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ws.academy.auction.api.dto.rq.users.CreateUserRq;
import ws.academy.auction.api.dto.rq.users.UpdateUserRq;
import ws.academy.auction.api.dto.rq.users.UserSearchRequest;
import ws.academy.auction.api.dto.rs.GuidRs;
import ws.academy.auction.api.dto.rs.ListPageData;
import ws.academy.auction.api.dto.rs.participants.ParticipantDetails;
import ws.academy.auction.api.dto.UserEmailData;
import ws.academy.auction.api.dto.rs.users.UserDetails;
import ws.academy.auction.api.dto.rs.users.UserList;
import ws.academy.auction.api.dto.rs.users.UserListItem;
import ws.academy.auction.core.entity.Participant;
import ws.academy.auction.core.entity.User;
import ws.academy.auction.core.enums.UserRole;
import ws.academy.auction.core.exception.NotFoundException;
import ws.academy.auction.core.mapper.ParticipantMapper;
import ws.academy.auction.core.mapper.UserMapper;
import ws.academy.auction.core.repository.ParticipantRepository;
import ws.academy.auction.core.repository.UserRepository;
import ws.academy.auction.core.service.EmailService;
import ws.academy.auction.core.service.UserService;
import ws.academy.auction.core.jpaspecifications.UserSpecification;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ParticipantMapper participantMapper;
    private final ParticipantRepository participantRepository;
    private final EmailService emailService;

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            ParticipantMapper participantMapper,
            ParticipantRepository participantRepository,
            @Qualifier("activateAccountEmailServiceImpl") EmailService emailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.participantMapper = participantMapper;
        this.participantRepository = participantRepository;
        this.emailService = emailService;
    }

    @Override
    public GuidRs addUser(CreateUserRq request) {
        User user = userMapper.buildUser(request);
        String hashCode = UUID.randomUUID().toString();
        user.setHash(hashCode);
        userRepository.save(user);
        emailService.sendEmailMessage(new UserEmailData(user.getEmail(), hashCode, user.getUsername()));
        return new GuidRs(user.getGuid());
    }

    @Override
    public UserDetails findByGuid(UUID guid) {
        User user = getUserOrThrow(guid);
        UserDetails userDetails = userMapper.buildUserDetails(user);
        ParticipantDetails participantDetails = getParticipantDetails(user);
        userDetails.setParticipant(participantDetails);
        return userDetails;
    }

    @Override
    public UserList getUsers(UserSearchRequest request) {
        int page = request.getPage() != null ? request.getPage() - 1 : 0;
        int size = request.getCount() != null ? request.getCount() : 30;

        Pageable pageable = getPageable(page, size, request);

        Specification<User> spec = getSpecification(request);

        Page<User> userPage = userRepository.findAll(spec, pageable);

        List<UserListItem> items = createUserListItem(userPage);

        ListPageData pageData = createListPageData(request, size, userPage);

        return createUserList(items, pageData);
    }

    @Override
    public void updateUserByGuid(UUID guid, UpdateUserRq request) {
        User user = getUserOrThrow(guid);

        UserRole newRole = UserRole.valueOf(request.getRole().toUpperCase());
        UserRole oldRole = user.getRole();

        user.setUsername(request.getFullName());
        user.setRole(newRole);

        boolean isNewRoleParticipant = newRole == UserRole.PARTICIPANT;
        boolean isOldRoleParticipant = oldRole == UserRole.PARTICIPANT;

        if (isNewRoleParticipant && !participantRepository.existsByUser(user)) {
            setParticipant(user);
        }
        if (!isNewRoleParticipant && isOldRoleParticipant) {
            participantRepository.deleteByUser(user);
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUserByGuid(UUID guid) {
        User user = getUserOrThrow(guid);

        participantRepository.deleteByUser(user);
        userRepository.deleteById(guid);
    }

    private User getUserOrThrow(UUID guid) {
        return userRepository.findById(guid)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден с id = " + guid));
    }

    private ParticipantDetails getParticipantDetails(User user) {
        Participant participant = participantRepository.findByUserGuid(user.getGuid());
        return participantMapper.buildParticipantDetails(participant);
    }

    private void setParticipant(User user) {
        Participant participant = participantMapper.buildParticipant(user);
        participant.setBalance(new BigDecimal("0.0"));
        participant.setUser(user);
        participantRepository.save(participant);
    }

    private Specification<User> getSpecification(UserSearchRequest request) {
        return Specification.where(
                        UserSpecification.hasName(request.getName()))
                .and(UserSpecification.hasRole(request.getRole()));
    }

    private ListPageData createListPageData(UserSearchRequest request, int size, Page<User> userPage) {
        return userMapper.buildListPageData(request, size, userPage);
    }

    private Pageable getPageable(int page, int size, UserSearchRequest request) {
        return PageRequest.of(page, size, Sort.by
                (Sort.Direction.fromString(request.getBy()), "username"));
    }

    private List<UserListItem> createUserListItem(Page<User> userPage) {
        return userPage.getContent().stream()
                .map(user -> {
                    UserListItem userListItem = userMapper.buildUserListItem(user);
                    if (participantRepository.existsByUser(user)) {
                        ParticipantDetails participantDetails = getParticipantDetails(user);
                        userListItem.setParticipant(participantDetails);
                    }
                    return userListItem;
                })
                .toList();
    }

    private UserList createUserList(List<UserListItem> userListItems, ListPageData pageData) {
        return UserList.builder()
                .items(userListItems)
                .page(pageData)
                .build();
    }
}
