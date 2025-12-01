package com.example.notification.shared.exceptions;

public class ExceptionUtils {

    public static NotificationAPIException exception(
            ErrorType errorType, int httpStatus, Exception exception
    ) {
        return new NotificationAPIException(errorType, httpStatus, exception);
    }

    public static NotificationAPIException badRequest(ErrorType errorType, Exception exception) {
        return exception(errorType, 400, exception);
    }

    public static NotificationAPIException internalError(ErrorType errorType, Exception exception) {
        return exception(errorType, 500, exception);
    }
}