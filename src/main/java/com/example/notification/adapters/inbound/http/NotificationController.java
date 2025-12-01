package com.example.notification.adapters.inbound.http;

import com.example.notification.adapters.converter.http.NotificationControllerConverter;
import com.example.notification.adapters.dto.http.NotificationBodyDto;
import com.example.notification.core.model.NotificationRequest;
import com.example.notification.core.ports.NotificationServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/notification")
public class NotificationController {

    private final NotificationControllerConverter converter;
    private final NotificationServicePort service;

    public NotificationController(
            NotificationControllerConverter converter,
            NotificationServicePort service) {
        this.converter = converter;
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationBodyDto body){
        try {
            NotificationRequest request = converter.convertToNotifyRequest(body);
            service.notify(request);
            return ResponseEntity.ok("");
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
