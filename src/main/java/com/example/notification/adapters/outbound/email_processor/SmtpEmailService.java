package com.example.notification.adapters.outbound.email_processor;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.shared.constants.ApplicationConstants;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Profile({"dev", "prod"})
public class SmtpEmailService implements EmailServicePort {

    private final JavaMailSender mailSender;

    public SmtpEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(EmailDto emailDto) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            helper.setTo(emailDto.to());
            helper.setSubject(emailDto.subject());
            helper.setText(emailDto.body(), true);
            helper.setFrom(ApplicationConstants.NO_REPLY_EMAIL);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage(), e);
        }
    }
}
