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
        NotificationRequest request = buildRequest(testCase);

        if (testCase.qrCode() != null) {
            when(qrCodeGeneratorService.generateBase64Qr(anyString()))
                    .thenReturn("QR_CODE_EM_BASE64");
        }

        service.notify(request);

        ArgumentCaptor<EmailDto> emailDtoCaptor = ArgumentCaptor.forClass(EmailDto.class);
        ArgumentCaptor<NotificationRequest> requestCaptor = ArgumentCaptor.forClass(NotificationRequest.class);
        verify(emailServicePort, times(1)).sendEmail(emailDtoCaptor.capture());
        verify(repository, times(1)).save(requestCaptor.capture());
        verify(repository).save(argThat(r -> !r.failedEmail()));
    }

    @ParameterizedTest
    @MethodSource("provideNotificationCases")
    void shouldSetFailedEmailWhenTemplateRenderingFails(TestCase testCase) {
        NotificationRequest request = buildRequest(testCase);

        when(templateRendererService.render(anyString(), anyMap()))
                .thenThrow(new RuntimeException("render fail"));

        service.notify(request);

        verify(emailServicePort, never()).sendEmail(any());
        verify(repository).save(argThat(NotificationRequest::failedEmail));
    }

    private NotificationRequest buildRequest(TestCase testCase) {
        return new NotificationRequest(
                "event_id",
                new NotificationRequest.User("John Doe", "john@example.com"),
                testCase.eventType(),
                new NotificationRequest.Payload(
                        123,
                        List.of(new ItemDto(1, "Item X", 1)),
                        BigDecimal.TEN,
                        testCase.qrCode()
                ),
                LocalDateTime.now(),
                null
        );
    }
}