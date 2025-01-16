package com.example.dossier.service;

import com.example.dossier.dto.EmailMessage;
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
    @Value("${spring.mail.username}")
    private String username;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmailMessage(EmailMessage emailMessage) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailMessage.getAddress());
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setSubject(emailMessage.getTheme().name());
        simpleMailMessage.setText(emailMessage.getText());
        try {
            emailSender.send(simpleMailMessage);
            logger.debug("Email sent successfully");
        } catch (MailSendException e) {
            logger.debug("Error sending email");
        }
    }
}
