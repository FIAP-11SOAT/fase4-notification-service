package com.example.notification.adapters.converter.http;

import com.example.notification.adapters.dto.http.NotificationBodyDto;
import com.example.notification.core.model.NotificationRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationControllerConverter {

    public NotificationRequest convertToNotifyRequest(NotificationBodyDto body){
        return new NotificationRequest(
                body.meta().eventId(),
                new NotificationRequest.User(
                        body.payload().customerName(),
                        body.payload().customerEmail()
                ),
                body.meta().eventName(),
                new NotificationRequest.Payload(
                        body.payload().orderId(),
                        body.payload().items(),
                        body.payload().amount(),
                        body.payload().qrCode()
                ),
                LocalDateTime.now(),
                null
        );
    }
}
