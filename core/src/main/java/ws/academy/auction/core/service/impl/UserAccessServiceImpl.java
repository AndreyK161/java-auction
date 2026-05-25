package ws.academy.auction.core.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ws.academy.auction.api.dto.rq.users.*;
import ws.academy.auction.api.dto.UserEmailData;
import ws.academy.auction.api.dto.rs.users.ResultMessageRs;
import ws.academy.auction.api.dto.rs.users.JwtRs;
import ws.academy.auction.core.entity.Participant;
import ws.academy.auction.core.entity.User;
import ws.academy.auction.core.enums.UserRole;
import ws.academy.auction.core.exception.InvalidAccountStateException;
import ws.academy.auction.core.mapper.ParticipantMapper;
import ws.academy.auction.core.mapper.UserMapper;
import ws.academy.auction.core.repository.ParticipantRepository;
import ws.academy.auction.core.repository.UserRepository;
import ws.academy.auction.core.service.EmailService;
import ws.academy.auction.core.service.KeycloakService;
import ws.academy.auction.core.service.UserAccessService;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class UserAccessServiceImpl implements UserAccessService {
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final KeycloakService keycloakService;
    private final EmailService emailService;
    private final ParticipantMapper participantMapper;
    private final UserMapper userMapper;

    public UserAccessServiceImpl(
            UserRepository userRepository,
            ParticipantRepository participantRepository,
            KeycloakService keycloakService,
            @Qualifier("activateAccountEmailServiceImpl") EmailService emailService,
            ParticipantMapper participantMapper,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.participantRepository = participantRepository;
        this.keycloakService = keycloakService;
        this.emailService = emailService;
        this.participantMapper = participantMapper;
        this.userMapper = userMapper;
    }

    @Override
    public JwtRs loginUser(AuthenticateUserRq request) {
        return keycloakService.authenticatedUser(request.getLogin(), request.getPassword());
    }

    @Override
    public JwtRs refreshTokenUser(RefreshTokenRq request) {
        return keycloakService.refreshTokenUser(request.getRefreshToken());
    }

    @Override
    public ResultMessageRs registerUser(RegisterUserRq request) {
        String hashCode = UUID.randomUUID().toString();
        User user = userMapper.buildRegisterUser(request);
        user.setHash(hashCode);
        user.setRole(UserRole.PARTICIPANT);
        userRepository.save(user);
        emailService.sendEmailMessage(new UserEmailData(user.getEmail(), user.getHash(), user.getUsername()));
        return new ResultMessageRs(true,
                "Ссылка с активацией аккаунта отправлена на адрес: " + user.getEmail());
    }

    @Override
    public ResultMessageRs activateUser(ActivateUserRq request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email адрес не найден"));

        validateActivationRequest(request, user);

        user.setHash(null);
        user.setActivated(true);

        initKeycloakAccount(request, user);

        if (user.getRole() == UserRole.PARTICIPANT) {
            activateParticipant(user);
        }
        userRepository.save(user);
        return new ResultMessageRs(true, "Аккаунт успешно активирован");
    }

    private void initKeycloakAccount(ActivateUserRq request, User user) {
        String keycloakGuid = keycloakService.createUser(user.getUsername(), user.getEmail(), request.getPassword());
        user.setGuidKeycloak(UUID.fromString(keycloakGuid));
    }

    private void validateActivationRequest(ActivateUserRq request, User user) {
        if (user.isActivated()) {
            throw new InvalidAccountStateException("Аккаунт уже активирован");
        }
        if (!Objects.equals(user.getHash(), request.getHash())) {
            throw new IllegalArgumentException("Hash не совпадает с переданным в письме");
        }
    }

    private void activateParticipant(User user) {
        Participant participant = participantMapper.buildParticipant(user);
        user.setRole(UserRole.PARTICIPANT);
        participant.setBalance(new BigDecimal("0.0"));
        participant.setUser(user);
        participantRepository.save(participant);
    }
}
