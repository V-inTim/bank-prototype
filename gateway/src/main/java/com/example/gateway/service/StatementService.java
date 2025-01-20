package com.example.gateway.service;

import com.example.gateway.client.StatementClient;
import com.example.gateway.dto.LoanOfferDto;
import com.example.gateway.dto.LoanStatementRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementService {
    private final StatementClient statementClient;

    @Autowired
    public StatementService(StatementClient statementClient) {
        this.statementClient = statementClient;
    }

    public List<LoanOfferDto> createStatement(LoanStatementRequestDto dto) {
        return statementClient.requestCreateStatement(dto);
    }

    public void applyOffer(LoanOfferDto dto) {
        statementClient.requestApplyOffer(dto);
    }
}
