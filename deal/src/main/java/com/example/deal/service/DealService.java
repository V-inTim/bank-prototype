package com.example.deal.service;

import com.example.deal.client.CalculatorClient;
import com.example.deal.dto.*;
import com.example.deal.entity.*;
import com.example.deal.exception.DbException;
import com.example.deal.mapper.ClientMapper;
import com.example.deal.mapper.CreditMapper;
import com.example.deal.mapper.OfferMapper;
import com.example.deal.repository.ClientRepository;
import com.example.deal.repository.CreditRepository;
import com.example.deal.repository.StatementRepository;
import com.example.deal.type.ApplicationStatus;
import com.example.deal.type.ChangeType;
import com.example.deal.type.CreditStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final CreditRepository creditRepository;
    private final ClientMapper clientMapper;
    private final OfferMapper offerMapper;
    private final CreditMapper creditMapper;
    private final CalculatorClient calculatorClient;

    private static final Logger logger = LoggerFactory.getLogger(DealService.class);

    @Autowired
    public DealService(ClientRepository clientRepository,
                       StatementRepository statementRepository,
                       CreditRepository creditRepository,
                       ClientMapper clientMapper,
                       OfferMapper offerMapper,
                       CreditMapper creditMapper,
                       CalculatorClient calculatorClient) {

        this.clientRepository = clientRepository;
        this.creditRepository = creditRepository;
        this.statementRepository = statementRepository;
        this.clientMapper = clientMapper;
        this.offerMapper = offerMapper;
        this.creditMapper = creditMapper;
        this.calculatorClient = calculatorClient;
    }

    public List<LoanOfferDto> createStatement(LoanStatementRequestDto requestData){
        Client client = clientMapper.dtoToClient(requestData);

        clientRepository.save(client);
        logger.debug("createStatement, save client");

        Statement statement = Statement.builder()
                .creationDate(LocalDateTime.now())
                .clientId(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .build();
        statementRepository.save(statement);
        logger.debug("createStatement, save statement");

        List<LoanOfferDto> offers = calculatorClient.requestOffers(requestData);
        offers.forEach(offer -> offer.setStatementId(statement.getStatementId()));

        return offers;
    }

    public void applyOffer(LoanOfferDto dto) throws DbException{
        UUID id = dto.getStatementId();
        // находим, если есть
        Optional<Statement> optionalStatement = statementRepository.findById(id);
        if (optionalStatement.isEmpty()){
            logger.debug("applyOffer, statement DbException");
            throw new DbException("Ресурс с данным id не сущетсвует.", HttpStatus.BAD_REQUEST);
        }
        logger.debug("applyOffer, find statement");
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
        logger.debug("applyOffer, save statement");
    }

    public void calculateCredit(FinishRegistrationRequestDto dto, UUID statementId) throws DbException{
        // поиск statement
        Optional<Statement> optionalStatement = statementRepository.findById(statementId);
        if (optionalStatement.isEmpty()){
            logger.debug("calculateCredit, statement DbException");
            throw new DbException("Ресурс с данным id не сущетсвует.", HttpStatus.BAD_REQUEST);
        }
        logger.debug("calculateCredit, find statement");
        Statement statement = optionalStatement.get();
        ApplicationStatus status = statement.getStatus();
        if (status != ApplicationStatus.APPROVED){
            logger.debug("calculateCredit, statement.status DbException");
            throw  new DbException("В заявке неподходящий статус.", HttpStatus.BAD_REQUEST);
        }
        // заполнение client
        Client client = statement.getClientId();
        client.setGender(dto.getGender());
        client.setMaritalStatus(dto.getMaritalStatus());
        client.setDependentAmount(dto.getDependentAmount());
        client.setAccountNumber(dto.getAccountNumber());

        EmploymentDto employmentDto = dto.getEmployment();
        Employment employment = Employment.builder()
                .status(employmentDto.getEmploymentStatus())
                .employerInn(employmentDto.getEmployerINN())
                .salary(employmentDto.getSalary())
                .position(employmentDto.getPosition())
                .workExperienceTotal(employmentDto.getWorkExperienceTotal())
                .workExperienceCurrent(employmentDto.getWorkExperienceCurrent())
                .build();
        client.setEmployment(employment);

        clientRepository.save(client);

        AppliedOffer offer = statement.getAppliedOffer();
        // заполнение ScoringDataDto и получение creditDto
        ScoringDataDto scoringDataDto = ScoringDataDto.builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .birthdate(client.getBirthDate())
                .gender(dto.getGender())
                .accountNumber(dto.getAccountNumber())
                .employment(dto.getEmployment())
                .maritalStatus(dto.getMaritalStatus())
                .passportSeries(client.getPassport().getSeries())
                .passportNumber(client.getPassport().getNumber())
                .passportIssueDate(dto.getPassportIssueDate())
                .passportIssueBranch(dto.getPassportIssueBranch())
                .term(offer.getTerm())
                .amount(offer.getRequestedAmount())
                .dependentAmount(dto.getDependentAmount())
                .isInsuranceEnabled(offer.getIsInsuranceEnabled())
                .isSalaryClient(offer.getIsSalaryClient()).build();

        CreditDto creditDto = calculatorClient.requestCalc(scoringDataDto);

        // сохранение credit
        Credit credit = creditMapper.dtoToCredit(creditDto);
        credit.setCreditStatus(CreditStatus.CALCULATED);

        credit = creditRepository.save(credit);
        logger.debug("calculateCredit, save credit");

        // сохранение statement
        List<StatusHistory> history = statement.getStatusHistory();
        history.add(StatusHistory.builder()
                .status(ApplicationStatus.CC_APPROVED)
                .changeType(ChangeType.AUTOMATIC)
                .time(LocalDateTime.now()).build());
        statement.setStatusHistory(history);
        statement.setStatus(ApplicationStatus.CC_APPROVED);
        statement.setCreditId(credit);
        statementRepository.save(statement);
        logger.debug("calculateCredit, save statement");
    }



}
