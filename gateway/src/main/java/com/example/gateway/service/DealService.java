package com.example.gateway.service;

import com.example.gateway.client.DealClient;
import com.example.gateway.dto.FinishRegistrationRequestDto;
import com.example.gateway.dto.SesCodeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DealService {
    private final DealClient dealClient;

    @Autowired
    public DealService(DealClient dealClient) {
        this.dealClient = dealClient;
    }

    public void calculateCredit(UUID statementId, FinishRegistrationRequestDto dto) {
        dealClient.requestCalculateCredit(dto, statementId);
    }

    public void sendDocuments(UUID statementId) {
        dealClient.requestDocumentsSend(statementId);
    }

    public void signDocuments(UUID statementId) {
        dealClient.requestDocumentsSign(statementId);
    }

    public void documentCode(UUID statementId, SesCodeDto dto) {
        dealClient.requestDocumentsCode(statementId, dto);
    }
}
