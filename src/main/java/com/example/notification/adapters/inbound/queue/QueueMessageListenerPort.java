package com.example.notification.adapters.inbound.queue;

import com.example.notification.adapters.dto.queue.MessageQueueDto;

public interface QueueMessageListenerPort {
    public void onMessage(MessageQueueDto message);
}
