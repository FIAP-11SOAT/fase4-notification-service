package com.example.notification.core.ports;

import com.example.notification.core.model.NotificationRequest;

import java.util.Map;

public interface TemplateServicePort {
    String getTemplateName();
    String getEmailSubject();
    Map<String, Object> getVariables(NotificationRequest.User user, NotificationRequest.Payload payload);
}
