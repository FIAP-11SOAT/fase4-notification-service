package com.example.notification.core.model;

import com.example.notification.shared.constants.EventTypeEnum;
import com.example.notification.shared.dto.ItemDto;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record NotificationRequest(
        String id,
        User user,
        EventTypeEnum eventType,
        Payload payload,
        LocalDateTime dateReceived,
        Boolean failedEmail
) {

    public NotificationRequest withFailedEmail(Boolean failed) {
        return new NotificationRequest(
                this.id,
                this.user,
                this.eventType,
                this.payload,
                this.dateReceived,
                failed
        );
    }

    public record User(String name, String email) {}
    public record Payload(Integer orderId, List<ItemDto> items, BigDecimal amount, String qrCode) {}
}
