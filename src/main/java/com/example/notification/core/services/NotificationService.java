package com.example.notification.core.services;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.adapters.outbound.email_processor.EmailServicePort;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import com.example.notification.core.ports.TemplateServicePort;
import com.example.notification.core.services.templates.*;
import com.example.notification.shared.constants.EventTypeEnum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class NotificationService implements NotificationServicePort {

    private final EmailServicePort emailService;
    private final TemplateRendererService templateRendererService;
    private final Map<EventTypeEnum, TemplateServicePort> templates;

    public NotificationService(
            EmailServicePort emailService,
            TemplateRendererService templateRendererService,
            List<TemplateServicePort> templates
    ) {
        this.emailService = emailService;
        this.templateRendererService = templateRendererService;
        this.templates = templates.stream().collect(Collectors.toMap(
                t -> switch (t.getTemplateName()) {
                    case "payment-created" -> EventTypeEnum.PAYMENT_CREATED;
                    case "payment-completed" -> EventTypeEnum.PAYMENT_COMPLETED;
                    case "payment-failed" -> EventTypeEnum.PAYMENT_FAILED;
                    case "production-completed" -> EventTypeEnum.PRODUCTION_COMPLETED;
                    default -> null;
                },
                t -> t
        ));
    }

    @Override
    public void notify(NotificationRequest request) {
        TemplateServicePort templateService = templates.get(request.eventType());

        if (templateService == null) {
            throw new IllegalArgumentException("No template found for event: " + request.eventType());
        }

        String htmlBody = templateRendererService.render(
                templateService.getTemplateName(),
                templateService.getVariables(request.user(), request.payload())
        );

        String to = request.user().email();
        String subject = request.eventType().getMessage();

        EmailDto emailDto = new EmailDto(to, subject, htmlBody);
        emailService.sendEmail(emailDto);
    }
}
