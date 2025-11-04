package com.example.notification.core.model;

import com.example.notification.adapters.outbound.dto.EmailDto;

public record NotificationRequest(String to, String subject, String body) {

    public EmailDto convertToEmailDto(NotificationRequest request){
        return new EmailDto(request.to(), request.subject(), request.body());
    }
}
