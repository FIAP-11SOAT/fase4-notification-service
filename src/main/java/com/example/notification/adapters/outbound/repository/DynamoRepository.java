package com.example.notification.adapters.outbound.repository;

import com.example.notification.core.model.NotificationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

@Repository
@Profile("prod")
public class DynamoRepository implements RepositoryPort {

    private final DynamoDbClient dynamoDbClient;
    private static final String TABLE = "notification-service-production-notifications";

    public DynamoRepository(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public void save(NotificationRequest messageRequest) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.fromS(messageRequest.id()));
        item.put("orderId", AttributeValue.fromN(messageRequest.payload().orderId().toString()));
        item.put("payload", AttributeValue.fromS(serialize(messageRequest)));
        dynamoDbClient.putItem(r -> r.tableName(TABLE).item(item));
    }

    @Override
    public NotificationRequest findById(String id) {
        Map<String, AttributeValue> key = Map.of(
                "id", AttributeValue.fromS(id)
        );

        var response = dynamoDbClient.getItem(r -> r.tableName(TABLE).key(key));

        if (!response.hasItem()) return null;

        return deserialize(response.item().get("payload").s());
    }

    private String serialize(NotificationRequest req) {
        try {
            return new ObjectMapper().writeValueAsString(req);
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    private NotificationRequest deserialize(String data) {
        try {
            return new ObjectMapper().readValue(data, NotificationRequest.class);
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}

