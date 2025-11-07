package com.example.notification.core.services.templates;

import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.TemplateServicePort;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentFailedTemplate implements TemplateServicePort {

    public PaymentFailedTemplate() {
    }

    @Override
    public String getTemplateName() {
        return "payment-failed";
    }

    @Override
    public Map<String, Object> getVariables(NotificationRequest.User user, NotificationRequest.Payload payload) {
        return Map.of();
    }

    @Override
    public String generateContent(NotificationRequest request) {
        return "";
    }
}
