package com.example.notification.core.services.templates;

import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.TemplateServicePort;
import com.example.notification.shared.utils.CurrencyFormatter;
import com.example.notification.shared.utils.QrCodeGenerator;

import java.util.Map;

public abstract class AbstractTemplateWithQrCode implements TemplateServicePort {

    private final QrCodeGenerator qrCodeGenerator;

    public AbstractTemplateWithQrCode(QrCodeGenerator qrCodeGenerator) {
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @Override
    public Map<String, Object> getVariables(NotificationRequest.User user, NotificationRequest.Payload payload) {
        String amount = CurrencyFormatter.convertToCurrencyString(payload.amount());
        return Map.of(
                "customerName", user.name(),
                "orderId", payload.orderId(),
                "items", payload.items(),
                "amount", amount,
                "qrCodeBase64", qrCodeGenerator.generateBase64Qr(payload.qrCode())
        );
    }
}
