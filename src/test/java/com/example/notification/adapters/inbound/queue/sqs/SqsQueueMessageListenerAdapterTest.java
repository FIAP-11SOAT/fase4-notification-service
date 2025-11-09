package com.example.notification.adapters.inbound.queue.sqs;

import com.example.notification.adapters.converter.queue.MessageQueueConverter;
import com.example.notification.adapters.dto.queue.MessageQueueDto;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import com.example.notification.shared.constants.EventTypeEnum;
import com.example.notification.shared.dto.ItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

class SqsQueueMessageListenerAdapterTest {

    private NotificationServicePort notificationServicePort;
    private MessageQueueConverter messageQueueConverter;
    private SqsQueueMessageListenerAdapter listenerAdapter;

    @BeforeEach
    void setUp() {
        notificationServicePort = mock(NotificationServicePort.class);
        messageQueueConverter = mock(MessageQueueConverter.class);
        listenerAdapter = new SqsQueueMessageListenerAdapter(notificationServicePort, messageQueueConverter);
    }

    @Test
    void shouldConvertMessageAndCallNotify() {
        MessageQueueDto message = new MessageQueueDto(
                new MessageQueueDto.MetaDataDto(
                        "event-1234",
                        LocalDate.now(),
                        "notification-service-queue",
                        "payment-service",
                        EventTypeEnum.PAYMENT_COMPLETED
                ),
                new MessageQueueDto.PayloadDto(
                        "John Doe",
                        "john.doe@example.com",
                        9876,
                        List.of(new ItemDto(1, "Hambúrguer Clássico", 1)),
                        BigDecimal.valueOf(29.90),
                        "QR_CODE_EXAMPLE"
                )
        );
        NotificationRequest expectedRequest = mock(NotificationRequest.class);

        when(messageQueueConverter.convertToNotifyRequest(message))
                .thenReturn(expectedRequest);

        listenerAdapter.onMessage(message);

        verify(messageQueueConverter, times(1)).convertToNotifyRequest(message);
        verify(notificationServicePort, times(1)).notify(expectedRequest);
    }

}