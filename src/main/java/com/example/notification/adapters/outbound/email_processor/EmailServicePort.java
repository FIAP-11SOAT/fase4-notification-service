package com.example.notification.adapters.outbound.email_processor;

import com.example.notification.adapters.outbound.dto.EmailDto;

public interface EmailServicePort {
    void sendEmail(EmailDto emailDto);
}
