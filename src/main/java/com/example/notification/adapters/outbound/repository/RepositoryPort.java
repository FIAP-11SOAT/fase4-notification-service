package com.example.notification.adapters.outbound.repository;

import com.example.notification.core.model.NotificationRequest;

public interface RepositoryPort {
    void save(NotificationRequest messageRequest);
    NotificationRequest findById(String id);
}
