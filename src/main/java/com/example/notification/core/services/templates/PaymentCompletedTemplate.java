package com.example.notification.core.services.templates;

import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.TemplateServicePort;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentCompletedTemplate implements TemplateServicePort {

    public PaymentCompletedTemplate() {
    }

    @Override
    public String getTemplateName() {
        return "payment-completed";
    }

    @Override
    public String getEmailSubject() {
        return "Totem: Pagamento aprovado \uD83E\uDD29";
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
