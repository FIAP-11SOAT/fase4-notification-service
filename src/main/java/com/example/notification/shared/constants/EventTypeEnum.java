package com.example.notification.shared.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum EventTypeEnum {

    PAYMENT_CREATED("payment-created-event", "Totem: Pedido criado e pagamento disponível"),
    PAYMENT_COMPLETED("payment-completed-event", "Totem: Pagamento aprovado!"),
    PAYMENT_FAILED("payment-failed-event", "Totem: Pagamento falhou"),
    PRODUCTION_COMPLETED("production-completed-event", "Totem: Seu pedido está pronto!");

    private final String value;
    private final String message;

    EventTypeEnum(String value, String message) {
        this.value = value.toLowerCase(Locale.ROOT);
        this.message = message;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EventTypeEnum fromValue(String value) {
        for (EventTypeEnum type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + value);
    }

    public String getMessage() {
        return message;
    }
}
