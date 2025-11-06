package com.example.notification.adapters.inbound.queue.sqs;

import com.example.notification.adapters.converter.queue.MessageQueueConverter;
import com.example.notification.adapters.dto.queue.MessageQueueDto;
import com.example.notification.adapters.inbound.queue.QueueMessageListenerPort;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
public class FakeQueueMessageListener implements QueueMessageListenerPort {

    private final NotificationServicePort service;
    private final MessageQueueConverter converter;


    public FakeQueueMessageListener(NotificationServicePort service, MessageQueueConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @Override
    public void onMessage(MessageQueueDto message) {
        NotificationRequest request = converter.convertToNotifyRequest(message);
        service.notify(request);
    }
}
