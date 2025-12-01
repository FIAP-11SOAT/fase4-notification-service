package com.example.notification.adapters.outbound.repository;

import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.model.NotificationRequest.Payload;
import com.example.notification.core.model.NotificationRequest.User;
import com.example.notification.shared.constants.ApplicationConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class DynamoRepositoryTest {

    @Mock
    private DynamoDbClient dynamoDbClient;

    @Mock
    private ObjectMapper objectMapper;

    private DynamoRepository repository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        repository = new DynamoRepository(dynamoDbClient, objectMapper);
    }

    private NotificationRequest buildNotification() {
        return new NotificationRequest(
                "abc123",
                new User("Amanda", "amanda@email.com"),
                null,
                new Payload(10, null, BigDecimal.TEN, "qrCode"),
                LocalDateTime.now(),
                false
        );
    }

    @Test
    void shouldSaveItemIntoDynamoDB() throws Exception {
        NotificationRequest request = buildNotification();

        when(objectMapper.writeValueAsString(request)).thenReturn("{json}");

        repository.save(request);

        ArgumentCaptor<Consumer<PutItemRequest.Builder>> captor =
                ArgumentCaptor.forClass(Consumer.class);

        verify(dynamoDbClient).putItem(captor.capture());

        PutItemRequest.Builder builder = PutItemRequest.builder();
        captor.getValue().accept(builder);
        PutItemRequest saved = builder.build();

        assertThat(saved.tableName()).isEqualTo(ApplicationConstants.TABLE);
        assertThat(saved.item()).containsEntry("id", AttributeValue.fromS("abc123"));
        assertThat(saved.item()).containsEntry("orderId", AttributeValue.fromN("10"));
        assertThat(saved.item()).containsEntry("payload", AttributeValue.fromS("{json}"));
    }

    @Test
    void shouldThrowExceptionWhenSerializeFails() throws Exception {
        NotificationRequest request = buildNotification();

        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("fail"));

        assertThatThrownBy(() -> repository.save(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("fail");
    }

    @Test
    void shouldReturnItemWhenExists() throws Exception {
        String json = "{json}";

        when(objectMapper.readValue(json, NotificationRequest.class))
                .thenReturn(buildNotification());

        GetItemResponse mockResponse = GetItemResponse.builder()
                .item(Map.of("payload", AttributeValue.fromS(json)))
                .build();

        when(dynamoDbClient.getItem(any(Consumer.class))).thenReturn(mockResponse);

        NotificationRequest result = repository.findById("abc123");

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo("abc123");
    }

    @Test
    void shouldReturnNullWhenItemDoesNotExist() {
        String id = "123";

        GetItemResponse emptyResponse = GetItemResponse.builder()
                .item(null)
                .build();

        when(dynamoDbClient.getItem(any(Consumer.class))).thenReturn(emptyResponse);

        NotificationRequest result = repository.findById(id);

        assertNull(result);
    }


    @Test
    void shouldThrowExceptionWhenDeserializeFails() throws Exception {
        GetItemResponse mockResponse = GetItemResponse.builder()
                .item(Map.of("payload", AttributeValue.fromS("{invalid}")))
                .build();

        when(dynamoDbClient.getItem(any(Consumer.class))).thenReturn(mockResponse);
        when(objectMapper.readValue(anyString(), eq(NotificationRequest.class)))
                .thenThrow(new RuntimeException("json-error"));

        assertThatThrownBy(() -> repository.findById("abc123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("json-error");
    }
}
