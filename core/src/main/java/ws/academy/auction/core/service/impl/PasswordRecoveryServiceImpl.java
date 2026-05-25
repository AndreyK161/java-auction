package ws.academy.auction.core.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ws.academy.auction.api.dto.rq.users.EmailRq;
import ws.academy.auction.api.dto.rq.users.ResetPasswordRq;
import ws.academy.auction.api.dto.UserEmailData;
import ws.academy.auction.api.dto.rs.users.ResultMessageRs;
import ws.academy.auction.core.entity.PasswordRecovery;
import ws.academy.auction.core.entity.User;
import ws.academy.auction.core.exception.InvalidAccountStateException;
import ws.academy.auction.core.repository.PasswordRecoveryRepository;
import ws.academy.auction.core.repository.UserRepository;
import ws.academy.auction.core.service.EmailService;
import ws.academy.auction.core.service.KeycloakService;
import ws.academy.auction.core.service.PasswordRecoveryService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordRecoveryServiceImpl implements PasswordRecoveryService {
    private final UserRepository userRepository;
    private final PasswordRecoveryRepository passwordRecoveryRepository;
    private final KeycloakService keycloakService;
    private final EmailService emailService;

    public PasswordRecoveryServiceImpl(
            UserRepository userRepository,
            PasswordRecoveryRepository passwordRecoveryRepository,
            KeycloakService keycloakService,
            @Qualifier("recoveryPasswordEmailServiceImpl") EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordRecoveryRepository = passwordRecoveryRepository;
        this.keycloakService = keycloakService;
        this.emailService = emailService;
    }

    @Override
    public ResultMessageRs sendPasswordRecoveryLink(EmailRq request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email адрес не найден"));
        if (!user.isActivated()) {
            throw new InvalidAccountStateException("Аккаунт не активирован");
        }

        String secret = UUID.randomUUID().toString();
        PasswordRecovery passwordRecovery = buildPasswordRecovery(secret, user);
        passwordRecoveryRepository.save(passwordRecovery);
        emailService.sendEmailMessage(new UserEmailData(user.getEmail(), secret, user.getUsername()));
        return new ResultMessageRs(true,
                "Ссылка на сброс пароля отправлена на адрес: " + user.getEmail());
    }

    @Override
    public ResultMessageRs resetPassword(ResetPasswordRq request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Email адрес не найден"));

        PasswordRecovery passwordRecovery = passwordRecoveryRepository
                .findByUserGuidAndRecoverySecret(user, request.getSecret())
                .orElseThrow(() -> new IllegalArgumentException("Secret не существует или не совпадает"));

        if (LocalDateTime.now().isAfter(passwordRecovery.getCreatedAt().plusDays(3))) {
            throw new IllegalArgumentException("Secret устарел, пожалуйста отправьте новый запрос");
        }

        passwordRecoveryRepository.delete(passwordRecovery);

        keycloakService.resetPassword(user.getGuidKeycloak().toString(), request.getNewPassword());
        userRepository.save(user);

        return new ResultMessageRs(true, "Пароль успешно изменен");
    }

    private PasswordRecovery buildPasswordRecovery(String secret, User user) {
        return PasswordRecovery.builder()
                .recoverySecret(secret)
                .createdAt(LocalDateTime.now())
                .userGuid(user)
                .build();
    }
}
