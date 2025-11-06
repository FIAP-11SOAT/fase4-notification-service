package com.example.notification.adapters.inbound.queue.sqs;

import com.example.notification.adapters.converter.queue.MessageQueueConverter;
import com.example.notification.adapters.dto.queue.MessageQueueDto;
import com.example.notification.adapters.inbound.queue.QueueMessageListenerPort;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Profile({"prod", "dev"})
public class SqsQueueMessageListenerAdapter implements QueueMessageListenerPort {

    private final NotificationServicePort service;
    private final MessageQueueConverter converter;

    public SqsQueueMessageListenerAdapter(NotificationServicePort service, MessageQueueConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @Override
    @SqsListener("${app.sqs.notification-queue-name}")
    public void onMessage(MessageQueueDto message) {
        System.out.println("PROD / DEV - Msg: " + message);
        NotificationRequest request = converter.convertToNotifyRequest(message);
        service.notify(request);
    }
}
