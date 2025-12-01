package com.example.notification.core.services.templates;

import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.TemplateServicePort;

import java.util.Map;

public abstract class AbstractTemplateNoQrCode implements TemplateServicePort {

    @Override
    public Map<String, Object> getVariables(NotificationRequest.User user, NotificationRequest.Payload payload) {
        return Map.of(
                "customerName", user.name(),
                "orderId", payload.orderId(),
                "items", payload.items()
        );
    }
}
