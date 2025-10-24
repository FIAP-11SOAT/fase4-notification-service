package com.example.notification.core.services;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.adapters.outbound.email_processor.EmailServicePort;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.mockito.*;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private EmailServicePort emailServicePort;

    private NotificationServicePort service;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        service = new NotificationService(emailServicePort);
    }

    @Test
    void shouldCallSendEmail(){
        NotificationRequest request = new NotificationRequest("", "", "");
        service.notify(request);
        ArgumentCaptor<EmailDto> captor = ArgumentCaptor.forClass(EmailDto.class);

        verify(emailServicePort, times(1)).sendEmail(captor.capture());
    }

}