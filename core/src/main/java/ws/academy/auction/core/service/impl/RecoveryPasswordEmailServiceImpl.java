package ws.academy.auction.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ws.academy.auction.api.dto.UserEmailData;
import ws.academy.auction.core.service.EmailService;

@Service
@RequiredArgsConstructor
public class RecoveryPasswordEmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${app.recovery.url}")
    private String recoveryUrl;
    @Value("${spring.mail.from}")
    private String from;

    @Override
    public void sendEmailMessage(UserEmailData data) {
        String recoveryLink = recoveryUrl + "?secret=" + data.getKey() + "&email=" + data.getEmail();
        String message = "Для сброса пароля перейдите по ссылке: " + recoveryLink;

        sendEmailMessage(data.getEmail(), message);
    }

    private void sendEmailMessage(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Recovery password");
        message.setText(text);
        javaMailSender.send(message);
    }
}
