package com.example.notification.adapters.outbound.repository;

import com.example.notification.core.model.NotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MongoRepositoryTest {

    @Mock
    private MongoTemplate mongoTemplate;

    private MongoRepository repository;

    @Value("${table.notifications}")
    private String TABLE;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new MongoRepository(mongoTemplate);
    }

    private NotificationRequest buildRequest() {
        return new NotificationRequest(
                "123",
                new NotificationRequest.User("John Doe", "john@example.com"),
                null,
                new NotificationRequest.Payload(
                        1,
                        List.of(),
                        BigDecimal.TEN,
                        null
                ),
                LocalDateTime.now(),
                false
        );
    }

    @Test
    void save_ShouldCallMongoTemplateSave() {
        NotificationRequest request = buildRequest();

        repository.save(request);

        verify(mongoTemplate, times(1))
                .save(eq(request), eq(TABLE));
    }

    @Test
    void save_ShouldHandleExceptionsGracefully() {
        NotificationRequest request = buildRequest();

        doThrow(new RuntimeException("Mongo error"))
                .when(mongoTemplate)
                .save(any(), anyString());

        assertDoesNotThrow(() -> repository.save(request));

        verify(mongoTemplate, times(1))
                .save(eq(request), eq(TABLE));
    }

    @Test
    void findById_ShouldReturnDocument() {
        NotificationRequest expected = buildRequest();

        when(mongoTemplate.findById("123", NotificationRequest.class, TABLE))
                .thenReturn(expected);

        NotificationRequest result = repository.findById("123");

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void findById_ShouldReturnNullWhenNotFound() {
        when(mongoTemplate.findById("123", NotificationRequest.class, TABLE))
                .thenReturn(null);

        NotificationRequest result = repository.findById("123");

        assertNull(result);
    }
}