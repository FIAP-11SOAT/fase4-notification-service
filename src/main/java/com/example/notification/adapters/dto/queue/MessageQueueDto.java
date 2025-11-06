package com.example.notification.adapters.dto.queue;

import com.example.notification.shared.constants.EventTypeEnum;
import com.example.notification.shared.dto.ItemDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record MessageQueueDto(
        MetaDataDto meta,
        PayloadDto payload
) {

    public record MetaDataDto(
            String eventId,
            LocalDate eventDate,
            String eventTarget,
            String eventSource,
            EventTypeEnum eventName
    ) {
    }

    public record PayloadDto(
            String customerName,
            String customerEmail,
            Integer orderId,
            List<ItemDto> items,
            BigDecimal amount,
            String qrCode
    ) {
    }
}

