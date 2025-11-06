package com.example.notification.adapters.dto.http;

import com.example.notification.shared.constants.EventTypeEnum;
import com.example.notification.shared.dto.ItemDto;

import java.math.BigDecimal;
import java.util.List;

public record NotificationBodyDto(
        MetaDataDto meta,
        PayloadDto payload
) {

    public record MetaDataDto(
            String eventId,
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
