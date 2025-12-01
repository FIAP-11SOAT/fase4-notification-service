package com.example.notification.core.services.templates;

import org.springframework.stereotype.Component;

@Component
public class ProductionCompletedTemplate extends AbstractTemplateNoQrCode {

    public ProductionCompletedTemplate() {
        // Construtor vazio intencionalmente — não há dependências neste fake.
    }

    @Override
    public String getTemplateName() {
        return "production-completed";
    }

    @Override
    public String getEmailSubject() {
        return "Totem: Seu pedido está pronto \uD83C\uDF54";
    }
}
