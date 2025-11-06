package com.example.notification.core.services;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.adapters.outbound.email_processor.EmailServicePort;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import com.example.notification.shared.constants.EventTypeEnum;
import com.example.notification.shared.dto.ItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;

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
        NotificationRequest notificationRequest = new NotificationRequest(
                new NotificationRequest.User(
                        "Amanda Costa",
                        "amanda.costa@example.com"
                ),
                EventTypeEnum.PAYMENT_COMPLETED,
                new NotificationRequest.Payload(
                        9876,
                        List.of(new ItemDto(1, "Hambúrguer Clássico", 1)),
                        BigDecimal.TEN,
                        "00020126580014BR.GOV.BCB.PIX0136e0e7c2b8-9f9b-4f21-b9e2-abc123456789520400005303986540545.805802BR5920Amanda Costa6009Sao Paulo62070503***6304ABCD"
                )
        );
        service.notify(notificationRequest);
        ArgumentCaptor<EmailDto> captor = ArgumentCaptor.forClass(EmailDto.class);

        verify(emailServicePort, times(1)).sendEmail(captor.capture());
    }

}