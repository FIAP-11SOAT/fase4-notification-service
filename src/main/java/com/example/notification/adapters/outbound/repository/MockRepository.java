package com.example.notification.adapters.outbound.repository;

import com.example.notification.core.model.NotificationRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Repository
@Profile("test")
public class MockRepository implements RepositoryPort{

    Map<String, NotificationRequest> items = new HashMap<>();

    @Override
    public void save(NotificationRequest messageRequest) {
        items.put(messageRequest.id(), messageRequest);
    }

    @Override
    public NotificationRequest findById(String id) {
        return items.get(id);
    }
}
