package com.example.notification.core.services.templates;

import com.example.notification.shared.utils.QrCodeGenerator;
import org.springframework.stereotype.Component;

@Component
public class PaymentFailedTemplate extends AbstractTemplateWithQrCode {

    public PaymentFailedTemplate(QrCodeGenerator qrCodeGenerator) {
        super(qrCodeGenerator);
    }

    @Override
    public String getTemplateName() {
        return "payment-failed";
    }

    @Override
    public String getEmailSubject() {
        return "Totem: Houve um problema com o pagamento \uD83E\uDD26\uD83C\uDFFD";
    }
}
