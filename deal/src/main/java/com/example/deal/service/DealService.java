package com.example.deal.service;

import com.example.deal.client.CalculatorClient;
import com.example.deal.dto.LoanOfferDto;
import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.entity.Client;
import com.example.deal.entity.Statement;
import com.example.deal.entity.StatusHistory;
import com.example.deal.exception.DbException;
import com.example.deal.mapper.ClientMapper;
import com.example.deal.mapper.OfferMapper;
import com.example.deal.repository.ClientRepository;
import com.example.deal.repository.StatementRepository;
import com.example.deal.type.ApplicationStatus;
import com.example.deal.type.ChangeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class DealService {
    private final ClientRepository clientRepository;
    private final StatementRepository statementRepository;
    private final ClientMapper clientMapper;
    private final OfferMapper offerMapper;
    private final CalculatorClient calculatorClient;

    @Autowired
    public DealService(ClientRepository clientRepository,
                       StatementRepository statementRepository,
                       ClientMapper clientMapper,
                       OfferMapper offerMapper,
                       CalculatorClient calculatorClient) {

        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.offerMapper = offerMapper;
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

    public void applyOffer(LoanOfferDto dto) throws DbException{
        UUID id = dto.getStatementId();
        // находим, если есть
        Optional<Statement> optionalStatement = statementRepository.findById(id);
        if (optionalStatement.isEmpty())
            throw new DbException("Resource with the given ID does not exist.", HttpStatus.BAD_REQUEST);
        Statement statement = optionalStatement.get();
        // заполняем
        statement.setStatus(ApplicationStatus.APPROVED);

        List<StatusHistory> history = statement.getStatusHistory();
        if (history == null)
            history = new ArrayList<StatusHistory>();
        history.add(StatusHistory.builder()
                .status(ApplicationStatus.APPROVED)
                .changeType(ChangeType.AUTOMATIC)
                .time(LocalDateTime.now()).build());
        statement.setStatusHistory(history);

        statement.setAppliedOffer(offerMapper.dtoToAppliedOffer(dto));
        // сохраняем изменения
        statementRepository.save(statement);
    }

}
