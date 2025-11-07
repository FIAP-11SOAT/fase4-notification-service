package com.example.notification.core.ports;

import com.example.notification.core.model.NotificationRequest;

import java.util.Map;

public interface TemplateServicePort {
    String getTemplateName();
    Map<String, Object> getVariables(NotificationRequest.User user, NotificationRequest.Payload payload);
    String generateContent(NotificationRequest request);
}
