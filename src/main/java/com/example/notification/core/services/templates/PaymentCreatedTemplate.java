package com.example.notification.core.services.templates;

import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.TemplateServicePort;
import com.example.notification.shared.utils.CurrencyFormatter;
import com.example.notification.shared.utils.QrCodeGenerator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentCreatedTemplate implements TemplateServicePort {

    private final QrCodeGenerator qrCodeGenerator;

    public PaymentCreatedTemplate(QrCodeGenerator qrCodeGenerator) {
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @Override
    public String getTemplateName() {
        return "payment-created";
    }

    @Override
    public String getEmailSubject() {
        return "Totem: Pedido criado e pagamento disponível \uD83D\uDE80";
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
