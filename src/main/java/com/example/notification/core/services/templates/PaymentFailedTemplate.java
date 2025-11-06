package com.example.notification.core.services.templates;

import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.TemplateServicePort;

public class PaymentFailedTemplate implements TemplateServicePort {

    @Override
    public String returnBodyMessage(NotificationRequest.User user, NotificationRequest.Payload payload) {
        return "Olá, " + user.name() + "! O pagamento para o pedido de número " + payload.orderId() + " falhou.";
    }
}
