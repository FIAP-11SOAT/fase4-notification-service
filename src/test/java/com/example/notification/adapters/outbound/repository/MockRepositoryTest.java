package com.example.notification.adapters.outbound.repository;

import com.example.notification.core.model.NotificationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockRepositoryTest {
    private MockRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MockRepository();
    }

    private NotificationRequest buildRequest(String id) {
        return new NotificationRequest(
                id,
                new NotificationRequest.User("John Doe", "john@example.com"),
                null,
                new NotificationRequest.Payload(
                        123,
                        List.of(),
                        BigDecimal.TEN,
                        null
                ),
                LocalDateTime.now(),
                false
        );
    }

    @Test
    void repository_ShouldStartEmpty() {
        assertTrue(repository.getMockRepository().isEmpty());
    }

    @Test
    void save_ShouldStoreRequestInMap() {
        NotificationRequest request = buildRequest("1");

        repository.save(request);

        assertTrue(repository.getMockRepository().containsKey("1"));
        assertEquals(request, repository.getMockRepository().get("1"));
    }

    @Test
    void save_ShouldOverrideExistingRequestWithSameId() {
        NotificationRequest req1 = buildRequest("1");
        NotificationRequest req2 = buildRequest("1");

        repository.save(req1);
        repository.save(req2);

        assertEquals(req2, repository.getMockRepository().get("1"));
        assertNotEquals(req1, repository.getMockRepository().get("1"));
    }

    @Test
    void findById_ShouldReturnStoredRequest() {
        NotificationRequest request = buildRequest("abc");

        repository.save(request);

        NotificationRequest result = repository.findById("abc");

        assertNotNull(result);
        assertEquals(request, result);
    }

    @Test
    void findById_ShouldReturnNullWhenNotFound() {
        assertNull(repository.findById("does-not-exist"));
    }
}