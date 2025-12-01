package com.example.notification.shared.exceptions;

import lombok.Getter;

@Getter
public enum ErrorType {

    INVALID_EVENT_TYPE(1, "invalid event type", "event type does not exists"),
    REPOSITORY_ERROR(2, "error on repository", "error processing entity on repository"),
    ERROR_SENDING_EMAIL(3, "email error", "error sendind email"),
    ERROR_GENERATING_QR_CODE(4, "QRCode error", "Erro ao gerar QR Code"),
    UTILITY_CLASS_ERROR(5, "utility class error", "This is a utility class and cannot be instantiated");

    private final int code;
    private final String name;
    public final String message;

    ErrorType(int code, String name, String message){
        this.code = code;
        this.name = name;
        this.message = message;
    }
}
