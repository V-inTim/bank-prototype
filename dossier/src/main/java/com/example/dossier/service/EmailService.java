package com.example.dossier.service;

import com.example.dossier.client.DealClient;
import com.example.dossier.dto.EmailMessage;
import com.example.dossier.type.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final DealClient dealClient;
    @Value("${spring.mail.username}")
    private String username;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public EmailService(JavaMailSender emailSender, DealClient dealClient) {
        this.emailSender = emailSender;
        this.dealClient = dealClient;
    }

    public void sendEmailMessage(EmailMessage emailMessage) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailMessage.getAddress());
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setSubject(emailMessage.getTheme().name());
        simpleMailMessage.setText(emailMessage.getText());
        try {
            emailSender.send(simpleMailMessage);
            logger.info("Email sent successfully");
            if (emailMessage.getTheme() == Topic.SEND_DOCUMENTS)
                dealClient.requestChangeStatus(emailMessage.getStatementId());
        } catch (MailSendException e) {
            logger.info("Error sending email");
        }
    }
}
