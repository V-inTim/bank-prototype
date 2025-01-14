package com.example.dossier.service;

import com.example.dossier.dto.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    public KafkaConsumerService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = {"finish-registration", "create-documents", "send-documents",
            "send-ses", "credit-issued", "statement-denied"}, groupId = "emailMessage")
    public void consume(EmailMessage message) {
        logger.debug("consume, {}", message.getTheme());
        emailService.sendEmailMessage(message);
    }
}
