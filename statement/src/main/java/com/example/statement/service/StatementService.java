package com.example.statement.service;

import com.example.statement.client.DealClient;
import com.example.statement.dto.LoanOfferDto;
import com.example.statement.dto.LoanStatementRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementService {

    private final DealClient client;

    @Autowired
    public StatementService(DealClient client){
        this.client = client;
    }

    public List<LoanOfferDto> createStatement(LoanStatementRequestDto requestDto){
        return client.requestStatement(requestDto);
    }

    public void applyOffer(LoanOfferDto requestDto){
        client.requestStatementOffer(requestDto);
    }

}
