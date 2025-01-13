package com.example.deal.service;

import com.example.deal.dto.EmailMessage;
import com.example.deal.entity.Statement;
import com.example.deal.type.ApplicationStatus;
import com.example.deal.type.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RefusalService {
    private final StatementService statementService;
    private final KafkaProducerService producerService;

    @Value("${deal.document.url}")
    private String url;

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    @Autowired
    public RefusalService(StatementService statementService, KafkaProducerService producerService) {
        this.statementService = statementService;
        this.producerService = producerService;
    }

    public void refuse(UUID statementId){
        Statement statement = statementService.getStatement(statementId);
        statementService.changeStatus(statement, ApplicationStatus.CC_DENIED);
        statementService.saveStatement(statement);
        logger.debug("refuse, save statement");

        String email = statement.getClientId().getEmail();
        String text = "Кредит отклонен. Вы не проходите по условиям.";
        EmailMessage emailMessage = EmailMessage.builder()
                .address(email)
                .theme(Topic.STATEMENT_DENIED)
                .statementId(statementId)
                .text(text).build();
        producerService.sendMessage(Topic.STATEMENT_DENIED.getDescription(), emailMessage);
        logger.debug("refuse, send message");
    }
}
