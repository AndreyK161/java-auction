package ws.academy.auction.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import ws.academy.auction.api.dto.UserEmailData;
import ws.academy.auction.core.service.EmailService;

@Service
@RequiredArgsConstructor
public class ActivateAccountEmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${app.activate.url}")
    private String activateUrl;
    @Value("${spring.mail.from}")
    private String from;

    @Override
    public void sendEmailMessage(UserEmailData data) {
        String activationLink = activateUrl + "?hash=" + data.getKey() + "&email=" + data.getEmail();
        String message = "Для активации аккаунта перейдите по ссылке: " + activationLink;

        sendEmailMessage(data.getEmail(), message);
    }

    private void sendEmailMessage(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Activate account");
        message.setText(text);
        javaMailSender.send(message);
    }
}
