package com.example.notification.core.ports;

import com.example.notification.core.model.NotificationRequest;
import org.springframework.stereotype.Component;

@Component
public interface NotificationServicePort {

    void notify(NotificationRequest request);
}
