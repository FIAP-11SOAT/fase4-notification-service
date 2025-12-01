package com.example.notification.core.services;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.adapters.outbound.email_processor.EmailServicePort;
import com.example.notification.adapters.outbound.repository.RepositoryPort;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import com.example.notification.core.ports.TemplateServicePort;
import com.example.notification.core.services.templates.*;
import com.example.notification.shared.constants.EventTypeEnum;
import com.example.notification.shared.exceptions.ErrorType;
import com.example.notification.shared.exceptions.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationService implements NotificationServicePort {

    private final EmailServicePort emailService;
    private final TemplateRendererService templateRendererService;
    private final Map<EventTypeEnum, TemplateServicePort> templates;
    private final RepositoryPort repository;

    public NotificationService(
            EmailServicePort emailService,
            TemplateRendererService templateRendererService,
            List<TemplateServicePort> templates,
            RepositoryPort repository
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
        this.repository = repository;
    }

    @Override
    public void notify(NotificationRequest request) {
        try {
            TemplateServicePort templateService = templates.get(request.eventType());

            if (templateService == null) {
                throw ExceptionUtils.badRequest(ErrorType.INVALID_EVENT_TYPE, null);
            }

            String htmlBody = templateRendererService.render(
                    templateService.getTemplateName(),
                    templateService.getVariables(request.user(), request.payload())
            );

            String to = request.user().email();
            String subject = templateService.getEmailSubject();

            EmailDto emailDto = new EmailDto(to, subject, htmlBody);
            emailService.sendEmail(emailDto);
            request = request.withFailedEmail(false);
            repository.save(request);
        } catch (Exception e) {
            request = request.withFailedEmail(true);
            repository.save(request);
            log.error("[NotificationService]: Error handling request {}", e.getMessage());
        }
    }
}
