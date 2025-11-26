package com.greenride.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @PostConstruct
    public void init() {
        // Only initialize if keys are present to avoid startup crashes during testing
        if (accountSid != null && !accountSid.isBlank() && authToken != null && !authToken.isBlank()) {
            Twilio.init(accountSid, authToken);
        }
    }

    public void sendWelcomeSms(String toPhoneNumber, String username) {
        // Robustness: Skip if credentials are not configured
        if (accountSid == null || accountSid.isBlank()) {
            System.err.println("Twilio credentials missing. SMS was not sent to: " + toPhoneNumber);
            return;
        }

        try {
            Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    "Welcome to GreenRide, " + username + "! Your account is now active. 🌿"
            ).create();
            System.out.println("SMS sent successfully to " + toPhoneNumber);
        } catch (Exception e) {
            // Log the error but do not throw exception, so the user is still registered
            System.err.println("Failed to send SMS: " + e.getMessage());
        }
    }
}