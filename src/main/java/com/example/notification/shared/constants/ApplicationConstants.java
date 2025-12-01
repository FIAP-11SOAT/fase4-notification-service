package com.example.notification.shared.constants;

import com.example.notification.shared.exceptions.ErrorType;
import com.example.notification.shared.exceptions.ExceptionUtils;

public class ApplicationConstants {

    private ApplicationConstants() {
        throw ExceptionUtils.internalError(ErrorType.UTILITY_CLASS_ERROR, null);
    }

    public static final String NO_REPLY_EMAIL = "teteuoliveira12@gmail.com";
    public static final String TABLE = "notification-service-production-notifications";
}
