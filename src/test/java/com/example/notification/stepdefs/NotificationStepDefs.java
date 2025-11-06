package com.example.notification.stepdefs;

import com.example.notification.adapters.dto.http.NotificationBodyDto;
import com.example.notification.adapters.outbound.email_processor.FakeEmailService;
import com.example.notification.shared.constants.EventTypeEnum;
import com.example.notification.shared.dto.ItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
public class NotificationStepDefs {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FakeEmailService fakeEmailService;

    private NotificationBodyDto body;
    private String jsonBody;

    @Given("a valid notification body")
    public void um_corpo_de_notificacao_valido() throws Exception {
        body = new NotificationBodyDto(
                new NotificationBodyDto.MetaDataDto("", "", "", EventTypeEnum.PRODUCTION_COMPLETED),
                new NotificationBodyDto.PayloadDto("Fulano de tal", "email@mail.com", 1234,
                        List.of(new ItemDto(1, "Hambúrguer Clássico", 1)),
                        BigDecimal.TEN,
                        ""
                )
        );
        jsonBody = objectMapper.writeValueAsString(body);
    }

    @When("a POST request is sent to {string}")
    public void uma_requisicao_post_e_enviada_para(String endpoint) throws Exception {
        mockMvc.perform(post(endpoint)
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Then("the FakeEmailService should receive the email")
    public void o_servico_fake_email_service_deve_receber_o_email() {
        assertFalse(fakeEmailService.getSentEmails().isEmpty());
        assertTrue(fakeEmailService.getSentEmails().getFirst().contains("email@mail.com"));
    }
}
