package com.example.notification.shared.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

public enum EventTypeEnum {

    PAYMENT_CREATED("payment-created-event"),
    PAYMENT_COMPLETED("payment-completed-event"),
    PAYMENT_FAILED("payment-failed-event"),
    PRODUCTION_COMPLETED("production-completed-event");

    private final String value;

    EventTypeEnum(String value) {
        this.value = value.toLowerCase(Locale.ROOT);
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
}
