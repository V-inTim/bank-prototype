package com.example.deal.service;

import com.example.deal.dto.EmailMessage;
import com.example.deal.entity.Statement;
import com.example.deal.exception.IncorrectSesCodeException;
import com.example.deal.type.ApplicationStatus;
import com.example.deal.type.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class DocumentService {
    private final KafkaProducerService producerService;
    private final SesCodeService sesCodeService;
    private final StatementService statementService;

    @Value("${deal.document.url}")
    private String url;

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    @Autowired
    public DocumentService(KafkaProducerService producerService,
                           SesCodeService sesCodeService,
                           StatementService statementService) {
        this.producerService = producerService;
        this.sesCodeService = sesCodeService;
        this.statementService = statementService;
    }

    public void sendDocuments(UUID statementId){
        Statement statement = statementService.getStatement(statementId);
        statementService.checkStatus(statement, ApplicationStatus.CC_APPROVED);
        statementService.changeStatus(statement, ApplicationStatus.PREPARE_DOCUMENTS);
        statementService.saveStatement(statement);
        logger.debug("sendDocuments, save statement");

        String email = statement.getClientId().getEmail();
        String text = String.format(
            "Запрошенные документы.\n В случае согласия перейдите по ссылке: %s/%s/code",
            this.url, statementId.toString()
        );

        EmailMessage emailMessage = EmailMessage.builder()
                .address(email)
                .theme(Topic.SEND_DOCUMENTS)
                .statementId(statementId)
                .text(text).build();
        producerService.sendMessage(Topic.SEND_DOCUMENTS.getDescription(), emailMessage);
        logger.debug("sendDocuments, send message");
    }

    public void signDocuments(UUID statementId){
        Statement statement = statementService.getStatement(statementId);

        String sesCode = sesCodeService.generateNumericCode(6);
        statement.setSesCode(sesCode);
        statementService.saveStatement(statement);
        logger.debug("signDocuments, save statement");

        String email = statement.getClientId().getEmail();
        String text = String.format(
            "Проверочный код: %s\n Перейдите по ссылке для проверки: %s/%s/sign",
                sesCode, this.url, statementId.toString()
        );

        EmailMessage emailMessage = EmailMessage.builder()
                .address(email)
                .theme(Topic.SEND_SES)
                .statementId(statementId)
                .text(text).build();
        producerService.sendMessage(Topic.SEND_SES.getDescription(), emailMessage);
        logger.debug("signDocuments, send message");
    }

    public void verifyCode(UUID statementId, String receivedSesCode){
        Statement statement = statementService.getStatement(statementId);

        String email = statement.getClientId().getEmail();
        String savedSesCode = statement.getSesCode();
        if (Objects.equals(savedSesCode, receivedSesCode)){
            statementService.checkStatus(statement, ApplicationStatus.PREPARE_DOCUMENTS); // после создания админского api исправить
            statementService.changeStatus(statement, ApplicationStatus.DOCUMENT_SIGNED);
            statementService.changeStatus(statement, ApplicationStatus.CREDIT_ISSUED);
            statementService.saveStatement(statement);
            logger.debug("verifyCode, save statement");
            EmailMessage emailMessage = EmailMessage.builder()
                    .address(email)
                    .theme(Topic.CREDIT_ISSUED)
                    .statementId(statementId)
                    .text("Кредит одобрен.").build();
            producerService.sendMessage(Topic.CREDIT_ISSUED.getDescription(), emailMessage);
            logger.debug("verifyCode, send message");
        } else {
            throw new IncorrectSesCodeException("Неправильный проверочный код.");
        }
    }

}
