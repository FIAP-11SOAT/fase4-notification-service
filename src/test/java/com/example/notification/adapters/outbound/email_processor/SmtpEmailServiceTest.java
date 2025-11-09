package com.example.notification.adapters.outbound.email_processor;

import com.example.notification.adapters.outbound.dto.EmailDto;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

class SmtpEmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private SmtpEmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private EmailDto create() {
        return new EmailDto(
                "john.doe@example.com",
                "Teste de envio de e-mail",
                "<h1>Olá John Doe!</h1><p>Seu pedido foi processado com sucesso.</p>"
        );
    }

    @Test
    void shouldSendEmailSuccessfully() throws Exception {
        EmailDto emailDto = create();
        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.sendEmail(emailDto);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void shouldThrowExceptionWhenMailSenderFails() {
        EmailDto emailDto = create();

        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("SMTP connection failed"));

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> emailService.sendEmail(emailDto)
        );

        org.junit.jupiter.api.Assertions.assertTrue(
                exception.getMessage().contains("Erro ao enviar e-mail")
        );
    }

}