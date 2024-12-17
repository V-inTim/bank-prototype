package com.example.deal.service;

import com.example.deal.client.CalculatorClient;
import com.example.deal.dto.LoanOfferDto;
import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.entity.Client;
import com.example.deal.entity.Statement;
import com.example.deal.mapper.ClientMapper;
import com.example.deal.repository.ClientRepository;
import com.example.deal.repository.StatementRepository;
import com.example.deal.type.ApplicationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class DealService {
    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;
    private final ClientMapper clientMapper;
    private final CalculatorClient calculatorClient;

    @Autowired
    public DealService(ClientRepository clientRepository,
                       StatementRepository statementRepository,
                       ClientMapper clientMapper,
                       CalculatorClient calculatorClient) {

        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.statementRepository = statementRepository;
        this.calculatorClient = calculatorClient;
    }

    public List<LoanOfferDto> createStatement(LoanStatementRequestDto requestData){
        Client client = clientMapper.dtoToClient(requestData);
        clientRepository.save(client);

        Statement statement = Statement.builder()
                .creationDate(LocalDateTime.now())
                .clientId(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .build();
        statementRepository.save(statement);

        List<LoanOfferDto> offers = calculatorClient.requestOffers(requestData);
        offers.forEach(offer -> offer.setStatementId(statement.getStatementId()));

        return offers;
    }

}
