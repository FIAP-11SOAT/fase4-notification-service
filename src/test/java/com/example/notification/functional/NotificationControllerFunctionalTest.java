package com.example.notification.functional;

import com.example.notification.adapters.dto.http.NotificationBodyDto;
import com.example.notification.adapters.outbound.email_processor.FakeEmailService;
import com.example.notification.shared.constants.EventTypeEnum;
import com.example.notification.shared.dto.ItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
        NotificationBodyDto body = new NotificationBodyDto(
                new NotificationBodyDto.MetaDataDto("", "", "", EventTypeEnum.PRODUCTION_COMPLETED),
                new NotificationBodyDto.PayloadDto("Fulano de tal", "email@mail.com", 1234,
                        List.of(new ItemDto(1, "Hambúrguer Clássico", 1)),
                        BigDecimal.TEN,
                        ""
                )
        );
        String jsonBody = objectMapper.writeValueAsString(body);

        mockMvc.perform(post("/v1/notification/")
                .contentType("application/json")
                .content(jsonBody)
        ).andExpect(status().isOk());

        assertFalse(fakeEmailService.getSentEmails().isEmpty());
        assertTrue(fakeEmailService.getSentEmails().getFirst().contains("email@mail.com"));
    }
}
