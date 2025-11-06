package com.example.notification.core.model;

import com.example.notification.shared.constants.EventTypeEnum;
import com.example.notification.shared.dto.ItemDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record NotificationRequest(
        User user,
        EventTypeEnum eventType,
        Payload payload,
        LocalDateTime dateReceived
) {

    public record User(
            String name,
            String email
    ){
    }

    public record Payload(
            Integer orderId,
            List<ItemDto> items,
            BigDecimal amount,
            String qrCode
    ) {
    }
}
