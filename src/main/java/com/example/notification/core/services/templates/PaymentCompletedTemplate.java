package com.example.notification.core.services.templates;

import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedTemplate extends AbstractTemplateNoQrCode {

    public PaymentCompletedTemplate() {
        // Construtor vazio intencionalmente — não há dependências neste fake.
    }

    @Override
    public String getTemplateName() {
        return "payment-completed";
    }

    @Override
    public String getEmailSubject() {
        return "Totem: Pagamento aprovado \uD83E\uDD29";
    }
}
