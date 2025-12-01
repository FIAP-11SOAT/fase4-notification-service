package com.example.notification.shared.exceptions;

import lombok.Getter;

@Getter
public class NotificationAPIException extends RuntimeException{

    private final int errorCode;
    private final String errorCodeMessage;
    private final String errorCodeName;
    private final int httpStatus;

    protected NotificationAPIException(ErrorType errorType, int httpStatus, Exception cause){
        super(errorType.getMessage(), cause);
        this.errorCode = errorType.getCode();
        this.errorCodeName = errorType.getName();
        this.errorCodeMessage = errorType.getMessage();
        this.httpStatus = httpStatus != 0 ? httpStatus : 500;
    }
}