package com.example.notification.adapters.outbound.repository;

import com.example.notification.core.model.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@Profile("dev")
public class MongoRepository implements RepositoryPort {

    private final MongoTemplate mongoTemplate;

    @Value("${table.notifications}")
    private String TABLE;

    public MongoRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void save(NotificationRequest messageRequest) {
        try{
            mongoTemplate.save(messageRequest, TABLE);
        } catch (Exception e){
            log.error("[MongoRepository]: Error saving document {}", e.getMessage());
        }
    }

    @Override
    public NotificationRequest findById(String id) {
        return mongoTemplate.findById(id, NotificationRequest.class, TABLE);
    }
}

