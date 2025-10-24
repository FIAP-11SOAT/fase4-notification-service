package com.example.notification.adapters.outbound.email_processor;

import com.example.notification.adapters.outbound.dto.EmailDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Service
@Profile("test")
@Component
public class FakeEmailService implements EmailServicePort {

    public FakeEmailService() {
    }
    
    private final List<String> sentEmails = new ArrayList<>();

    @Override
    public void sendEmail(EmailDto emailDto) {
        sentEmails.add(emailDto.to());
    }
}
