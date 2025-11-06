package com.example.notification.core.services;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.adapters.outbound.email_processor.EmailServicePort;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import com.example.notification.core.ports.TemplateServicePort;
import com.example.notification.core.services.templates.PaymentCompletedTemplate;
import com.example.notification.core.services.templates.PaymentCreatedTemplate;
import com.example.notification.core.services.templates.PaymentFailedTemplate;
import com.example.notification.core.services.templates.ProductionCompletedTemplate;
import com.example.notification.shared.constants.EventTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class NotificationService implements NotificationServicePort {

    private final EmailServicePort emailService;

    @Autowired
    public NotificationService(EmailServicePort emailService) {
        this.emailService = emailService;
    }

    @Override
    public void notify(NotificationRequest request) {

        Optional<TemplateServicePort> templateServicePort = selectTemplateService(request.eventType());

        if(templateServicePort.isPresent()){
            TemplateServicePort templateService = templateServicePort.get();
            String messageBody = templateService.returnBodyMessage(request.user(), request.payload());
            String to = request.user().email();
            String subject = request.eventType().getMessage();
            System.out.println("\n to: " + to);
            System.out.println("\n subject: " + subject);
            System.out.println("\n message: " + messageBody);

            EmailDto emailDto = new EmailDto(to, subject, messageBody);
            emailService.sendEmail(emailDto);
        }
    }

    private Optional<TemplateServicePort> selectTemplateService(EventTypeEnum eventType){
        if (eventType.equals(EventTypeEnum.PAYMENT_CREATED)){
            return Optional.of(new PaymentCreatedTemplate());
        }

        if (eventType.equals(EventTypeEnum.PAYMENT_COMPLETED)){
            return Optional.of(new PaymentCompletedTemplate());
        }

        if (eventType.equals(EventTypeEnum.PAYMENT_FAILED)){
            return Optional.of(new PaymentFailedTemplate());
        }

        if (eventType.equals(EventTypeEnum.PRODUCTION_COMPLETED)){
            return Optional.of(new ProductionCompletedTemplate());
        }

        return Optional.empty();
    }
}
