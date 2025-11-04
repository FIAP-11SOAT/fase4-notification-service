package com.example.notification.core.services;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.adapters.outbound.email_processor.EmailServicePort;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NotificationService implements NotificationServicePort {

    private final EmailServicePort emailService;

    @Autowired
    public NotificationService(EmailServicePort emailService) {
        this.emailService = emailService;
    }

    @Override
    public void notify(NotificationRequest request) {
        EmailDto emailDto = request.convertToEmailDto(request);
        emailService.sendEmail(emailDto);
    }
}
