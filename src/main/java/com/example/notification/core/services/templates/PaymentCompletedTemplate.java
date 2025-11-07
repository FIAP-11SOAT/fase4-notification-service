package com.example.notification.core.services.templates;

import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.TemplateServicePort;
import com.example.notification.shared.utils.QrCodeGenerator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
public class PaymentCompletedTemplate implements TemplateServicePort {

    private final TemplateEngine templateEngine;
    private final QrCodeGenerator qrCodeGenerator;

    public PaymentCompletedTemplate(TemplateEngine templateEngine, QrCodeGenerator qrCodeGenerator) {
        this.templateEngine = templateEngine;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @Override
    public String getTemplateName() {
        return "payment-completed";
    }

    @Override
    public Map<String, Object> getVariables(NotificationRequest.User user, NotificationRequest.Payload payload) {
        return Map.of(
                "customerName", user.name(),
                "orderId", payload.orderId(),
                "items", payload.items(),
                "qrCodeBase64", qrCodeGenerator.generateBase64Qr(payload.qrCode())
        );
    }

    @Override
    public String generateContent(NotificationRequest request) {
        System.out.println("\n WRONG TEMPLATE SERVICE");
        Context context = new Context();
        context.setVariable("customerName", request.user().name());
        context.setVariable("orderId", request.payload().orderId());
        context.setVariable("items", request.payload().items());

        String qrCodeBase64 = qrCodeGenerator.generateBase64Qr(request.payload().qrCode());
        context.setVariable("qrCodeBase64", qrCodeBase64);

        return templateEngine.process("payment-completed", context);
    }
}
