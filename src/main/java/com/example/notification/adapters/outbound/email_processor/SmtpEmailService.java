package com.example.notification.adapters.outbound.email_processor;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.shared.constants.ApplicationConstants;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
@Component
public class SmtpEmailService implements EmailServicePort {

    private final JavaMailSender mailSender;

    public SmtpEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(EmailDto emailDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailDto.to());
        message.setSubject(emailDto.subject());
        message.setText(emailDto.body());
        message.setFrom(ApplicationConstants.NO_REPLY_EMAIL);
        mailSender.send(message);
    }
}
