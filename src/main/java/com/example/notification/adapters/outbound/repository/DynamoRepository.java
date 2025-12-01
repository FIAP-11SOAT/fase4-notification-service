package com.example.notification.adapters.outbound.repository;

import com.example.notification.core.model.NotificationRequest;
import com.example.notification.shared.constants.ApplicationConstants;
import com.example.notification.shared.exceptions.ErrorType;
import com.example.notification.shared.exceptions.ExceptionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@Profile("prod")
public class DynamoRepository implements RepositoryPort {

    private final DynamoDbClient dynamoDbClient;
    private final ObjectMapper objectMapper;

    public DynamoRepository(DynamoDbClient dynamoDbClient, ObjectMapper objectMapper) {
        this.dynamoDbClient = dynamoDbClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(NotificationRequest messageRequest) {
        try{
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("id", AttributeValue.fromS(messageRequest.id()));
            item.put("orderId", AttributeValue.fromN(messageRequest.payload().orderId().toString()));
            item.put("payload", AttributeValue.fromS(serialize(messageRequest)));
            dynamoDbClient.putItem(r -> r.tableName(ApplicationConstants.TABLE).item(item));
        } catch (Exception e){
            log.error("[DynamoRepository]: Error save() {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public NotificationRequest findById(String id) {
        Map<String, AttributeValue> key = Map.of(
                "id", AttributeValue.fromS(id)
        );

        var response = dynamoDbClient.getItem(r -> r.tableName(ApplicationConstants.TABLE).key(key));

        if (!response.hasItem()) return null;

        return deserialize(response.item().get("payload").s());
    }

    private String serialize(NotificationRequest req) {
        try {
            return objectMapper.writeValueAsString(req);
        } catch (Exception e) {
            log.error("[DynamoRepository]: Error serialize() {}", e.getMessage());
            throw ExceptionUtils.internalError(ErrorType.REPOSITORY_ERROR, e);
        }
    }

    private NotificationRequest deserialize(String data) {
        try {
            return objectMapper.readValue(data, NotificationRequest.class);
        } catch (Exception e) {
            log.error("[DynamoRepository]: Error deserialize() {}", e.getMessage());
            throw ExceptionUtils.internalError(ErrorType.REPOSITORY_ERROR, e);
        }
    }
}

