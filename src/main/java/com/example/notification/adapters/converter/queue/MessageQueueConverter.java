package com.example.notification.adapters.converter.queue;

import com.example.notification.adapters.dto.queue.MessageQueueDto;
import com.example.notification.core.model.NotificationRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessageQueueConverter {

    public NotificationRequest convertToNotifyRequest(MessageQueueDto message){
        return new NotificationRequest(
                new NotificationRequest.User(
                        message.payload().customerName(),
                        message.payload().customerEmail()
                ),
                message.meta().eventName(),
                new NotificationRequest.Payload(
                        message.payload().orderId(),
                        message.payload().items(),
                        message.payload().amount(),
                        message.payload().qrCode()
                ),
                LocalDateTime.now()
        );
    }
}
