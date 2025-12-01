package com.example.notification.adapters.outbound.email_processor;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.shared.constants.ApplicationConstants;
import com.example.notification.shared.exceptions.ErrorType;
import com.example.notification.shared.exceptions.ExceptionUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
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
            log.error("[SmtpEmailService]: Error sending email {}", e.getMessage());
            throw ExceptionUtils.internalError(ErrorType.ERROR_SENDING_EMAIL, e);
        }
    }
}
