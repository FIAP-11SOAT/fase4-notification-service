package com.example.notification.adapters.converter.queue;

import com.example.notification.adapters.dto.queue.MessageQueueDto;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.shared.constants.EventTypeEnum;
import com.example.notification.shared.dto.ItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageQueueConverterTest {

    private MessageQueueConverter converter;

    @BeforeEach
    void setUp() {
        converter = new MessageQueueConverter();
    }

    @Test
    void shouldConvertMessageQueueDtoToNotificationRequest() {
        MessageQueueDto message = new MessageQueueDto(
                new MessageQueueDto.MetaDataDto(
                        "event-1234",
                        LocalDate.of(2025, 11, 9),
                        "notification-service-queue",
                        "payment-service",
                        EventTypeEnum.PAYMENT_COMPLETED
                ),
                new MessageQueueDto.PayloadDto(
                        "John Doe",
                        "john.doe@example.com",
                        9876,
                        List.of(new ItemDto(1, "Hambúrguer Clássico", 1)),
                        BigDecimal.valueOf(49.90),
                        "QR_CODE_ABC"
                )
        );

        NotificationRequest request = converter.convertToNotifyRequest(message);

        assertNotNull(request);
        assertEquals("John Doe", request.user().name());
        assertEquals("john.doe@example.com", request.user().email());
        assertEquals(EventTypeEnum.PAYMENT_COMPLETED, request.eventType());
        assertEquals(9876, request.payload().orderId());
        assertEquals(1, request.payload().items().size());
        assertEquals("Hambúrguer Clássico", request.payload().items().get(0).name());
        assertEquals(BigDecimal.valueOf(49.90), request.payload().amount());
        assertEquals("QR_CODE_ABC", request.payload().qrCode());
        assertNotNull(request.dateReceived());
        assertTrue(request.dateReceived().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void shouldThrowNullPointerIfMessageIsNull() {
        MessageQueueDto message = null;

        assertThrows(NullPointerException.class, () -> converter.convertToNotifyRequest(message));
    }

}