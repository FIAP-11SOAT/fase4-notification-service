package com.example.notification.shared.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QrCodeGeneratorTest {

    private QrCodeGenerator qrCodeGenerator;

    @BeforeEach
    void setUp() {
        qrCodeGenerator = new QrCodeGenerator();
    }

    @Test
    void shouldGenerateBase64QrSuccessfully() {
        String content = "https://meusite.com/pagamento/123";

        String base64Qr = qrCodeGenerator.generateBase64Qr(content);

        assertNotNull(base64Qr);
        assertFalse(base64Qr.isBlank());

        assertDoesNotThrow(() -> {
            byte[] decoded = java.util.Base64.getDecoder().decode(base64Qr);
            assertTrue(decoded.length > 0);
        });
    }

    @Test
    void shouldThrowExceptionWhenContentIsNull() {
        String content = null;

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                qrCodeGenerator.generateBase64Qr(content)
        );

        assertTrue(exception.getMessage().contains("Erro ao gerar QR Code"));
    }
}
