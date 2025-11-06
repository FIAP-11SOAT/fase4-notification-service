package com.example.notification.core.services.templates;

import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.TemplateServicePort;

public class PaymentCreatedTemplate implements TemplateServicePort {

    public PaymentCreatedTemplate() {
    }

    @Override
    public String returnBodyMessage(NotificationRequest.User user, NotificationRequest.Payload payload) {
        return "Olá, " + user.name() + "! O seu pedido de número  " + payload.orderId() + " foi criado e está disponível para pagamento pelo qr_code: " + payload.qrCode();
    }
}
