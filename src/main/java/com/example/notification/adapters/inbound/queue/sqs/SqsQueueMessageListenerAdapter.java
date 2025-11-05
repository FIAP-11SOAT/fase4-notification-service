package com.example.notification.adapters.inbound.queue.sqs;

import com.example.notification.adapters.inbound.queue.QueueMessageListenerPort;
import com.example.notification.core.ports.NotificationServicePort;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"prod", "dev"})
public class SqsQueueMessageListenerAdapter implements QueueMessageListenerPort {

    private final NotificationServicePort service;

    public SqsQueueMessageListenerAdapter(NotificationServicePort service) {
        this.service = service;
    }

    @Override
    @SqsListener("${app.sqs.notification-queue-name}")
    public void onMessage(String message) {
        System.out.println("PROD / DEV - Msg: " + message);
    }
}
