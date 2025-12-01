package com.example.notification.core.services.templates;

import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.TemplateServicePort;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ProductionCompletedTemplate implements TemplateServicePort {

    public ProductionCompletedTemplate() {
        // Construtor vazio intencionalmente — não há dependências neste fake.
    }

    @Override
    public String getTemplateName() {
        return "production-completed";
    }

    @Override
    public String getEmailSubject() {
        return "Totem: Seu pedido está pronto \uD83C\uDF54";
    }

    @Override
    public Map<String, Object> getVariables(NotificationRequest.User user, NotificationRequest.Payload payload) {
        return Map.of(
                "customerName", user.name(),
                "orderId", payload.orderId(),
                "items", payload.items()
        );
    }
}
