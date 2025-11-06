package com.example.notification.core.ports;

import com.example.notification.core.model.NotificationRequest;

public interface TemplateServicePort {

    public String returnBodyMessage(NotificationRequest.User user, NotificationRequest.Payload payload);
}
