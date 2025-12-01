package com.example.notification.shared.constants;

import com.example.notification.shared.exceptions.ErrorType;
import com.example.notification.shared.exceptions.NotificationAPIException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EventTypeEnumTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldDeserializeIgnoringCase() throws Exception {
        EventTypeEnum result = mapper.readValue("\"PaYmEnT-fAiLeD-EvEnT\"", EventTypeEnum.class);

        assertThat(result).isEqualTo(EventTypeEnum.PAYMENT_FAILED);
    }

    @Test
    void shouldThrowExceptionForInvalidType() {
        assertThatThrownBy(() ->
                EventTypeEnum.fromValue("invalid-type")
        )
                .isInstanceOf(NotificationAPIException.class)
                .hasMessage(ErrorType.INVALID_EVENT_TYPE.getMessage());
    }

}