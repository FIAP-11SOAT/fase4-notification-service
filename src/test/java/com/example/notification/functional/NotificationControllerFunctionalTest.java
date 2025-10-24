package com.example.notification.functional;

import com.example.notification.adapters.dto.http.NotificationBodyDto;
import com.example.notification.adapters.outbound.email_processor.FakeEmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class NotificationControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FakeEmailService fakeEmailService;

    @Test
    void shouldSendEmailUsingFakeEmailService() throws Exception{
        NotificationBodyDto body = new NotificationBodyDto("email@mail.com", "Assunto", "Conteúdo do email");
        String jsonBody = objectMapper.writeValueAsString(body);

        mockMvc.perform(post("/v1/notification/")
                .contentType("application/json")
                .content(jsonBody)
        ).andExpect(status().isOk());

        assertFalse(fakeEmailService.getSentEmails().isEmpty());
        assertTrue(fakeEmailService.getSentEmails().getFirst().contains("email@mail.com"));
    }
}
