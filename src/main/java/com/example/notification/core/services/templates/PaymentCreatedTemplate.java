package com.example.notification.core.services.templates;

import com.example.notification.shared.utils.QrCodeGenerator;
import org.springframework.stereotype.Component;

@Component
public class PaymentCreatedTemplate extends AbstractTemplateWithQrCode {

    public PaymentCreatedTemplate(QrCodeGenerator qrCodeGenerator) {
        super(qrCodeGenerator);
    }

    @Override
    public String getTemplateName() {
        return "payment-created";
    }

    @Override
    public String getEmailSubject() {
        return "Totem: Pedido criado e pagamento disponível \uD83D\uDE80";
    }
}
