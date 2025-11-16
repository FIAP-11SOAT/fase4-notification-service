package com.example.notification.core.services;

import com.example.notification.adapters.outbound.dto.EmailDto;
import com.example.notification.adapters.outbound.email_processor.EmailServicePort;
import com.example.notification.adapters.outbound.repository.RepositoryPort;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import com.example.notification.core.ports.TemplateServicePort;
import com.example.notification.core.services.templates.*;
import com.example.notification.shared.constants.EventTypeEnum;
import com.example.notification.shared.dto.ItemDto;
import com.example.notification.shared.utils.QrCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private EmailServicePort emailServicePort;

    @Mock
    private TemplateRendererService templateRendererService;

    @Mock
    private QrCodeGenerator qrCodeGeneratorService;

    @Mock
    private RepositoryPort repository;

    private NotificationServicePort service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        List<TemplateServicePort> templates = List.of(
                new PaymentCompletedTemplate(),
                new PaymentCreatedTemplate(qrCodeGeneratorService),
                new PaymentFailedTemplate(qrCodeGeneratorService),
                new ProductionCompletedTemplate()
        );
        service = new NotificationService(emailServicePort, templateRendererService, templates, repository);
    }

    private record TestCase(EventTypeEnum eventType, String qrCode) {}

    private static Stream<TestCase> provideNotificationCases() {
        return Stream.of(
                new TestCase(EventTypeEnum.PAYMENT_CREATED, "PIX_CODE"),
                new TestCase(EventTypeEnum.PAYMENT_FAILED, "PIX_CODE"),
                new TestCase(EventTypeEnum.PAYMENT_COMPLETED, null),
                new TestCase(EventTypeEnum.PRODUCTION_COMPLETED, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNotificationCases")
    void shouldCallSendEmailForAllEventTypes(TestCase testCase) {
        NotificationRequest notificationRequest = new NotificationRequest(
                "event_id",
                new NotificationRequest.User("John Doe", "john.doe@example.com"),
                testCase.eventType(),
                new NotificationRequest.Payload(
                        9876,
                        List.of(new ItemDto(1, "Hambúrguer Clássico", 1)),
                        BigDecimal.TEN,
                        testCase.qrCode()
                ),
                LocalDateTime.now(),
                null
        );

        if (testCase.qrCode() != null) {
            when(qrCodeGeneratorService.generateBase64Qr(anyString()))
                    .thenReturn("QR_CODE_EM_BASE64");
        }

        service.notify(notificationRequest);

        ArgumentCaptor<EmailDto> captor = ArgumentCaptor.forClass(EmailDto.class);
        verify(emailServicePort, times(1)).sendEmail(captor.capture());
    }
}