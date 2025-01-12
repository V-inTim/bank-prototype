package com.example.deal.service;

import com.example.deal.client.CalculatorClient;
import com.example.deal.dto.*;
import com.example.deal.entity.*;
import com.example.deal.mapper.ClientMapper;
import com.example.deal.mapper.CreditMapper;
import com.example.deal.mapper.OfferMapper;
import com.example.deal.repository.ClientRepository;
import com.example.deal.repository.CreditRepository;
import com.example.deal.type.ApplicationStatus;
import com.example.deal.type.CreditStatus;
import com.example.deal.type.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class DealService {
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;
    private final StatementService statementService;
    private final ClientMapper clientMapper;
    private final OfferMapper offerMapper;
    private final CreditMapper creditMapper;
    private final CalculatorClient calculatorClient;
    private  final KafkaProducerService producerService;

    @Value("${deal.document.url}")
    private String url;

    private static final Logger logger = LoggerFactory.getLogger(DealService.class);

    @Autowired
    public DealService(ClientRepository clientRepository,
                       CreditRepository creditRepository,
                       StatementService statementService,
                       ClientMapper clientMapper,
                       OfferMapper offerMapper,
                       CreditMapper creditMapper,
                       CalculatorClient calculatorClient,
                       KafkaProducerService producerService) {

        this.clientRepository = clientRepository;
        this.creditRepository = creditRepository;
        this.statementService = statementService;
        this.clientMapper = clientMapper;
        this.offerMapper = offerMapper;
        this.creditMapper = creditMapper;
        this.calculatorClient = calculatorClient;
        this.producerService = producerService;
    }

    public List<LoanOfferDto> createStatement(LoanStatementRequestDto requestData){
        Client client = clientMapper.dtoToClient(requestData);

        clientRepository.save(client);
        logger.debug("createStatement, save client");

        Statement statement = Statement.builder()
                .creationDate(LocalDateTime.now())
                .clientId(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .statusHistory(new ArrayList<>())
                .build();
        statementService.saveStatement(statement);
        logger.debug("createStatement, save statement");

        List<LoanOfferDto> offers = calculatorClient.requestOffers(requestData);
        offers.forEach(offer -> offer.setStatementId(statement.getStatementId()));

        return offers;
    }

    public void applyOffer(LoanOfferDto dto){
        UUID statementId = dto.getStatementId();
        Statement statement = statementService.getStatement(statementId);

        statementService.checkStatus(statement, ApplicationStatus.PREAPPROVAL);
        statementService.changeStatus(statement, ApplicationStatus.APPROVED);
        statement.setAppliedOffer(offerMapper.dtoToAppliedOffer(dto));
        statementService.saveStatement(statement);

        String email = statement.getClientId().getEmail();
        String text = "Ваша заявка предварительно одобрена, завершите оформление";
        EmailMessage emailMessage = EmailMessage.builder()
                .address(email)
                .theme(Topic.FINISH_REGISTRATION)
                .statementId(statementId)
                .text(text).build();
        producerService.sendMessage(Topic.FINISH_REGISTRATION.getDescription(), emailMessage);
    }

    public void calculateCredit(FinishRegistrationRequestDto dto, UUID statementId){

        Statement statement = statementService.getStatement(statementId);
        statementService.checkStatus(statement, ApplicationStatus.APPROVED);

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

        CreditDto creditDto = calculatorClient.requestCalc(scoringDataDto, statementId);

        // сохранение credit
        Credit credit = creditMapper.dtoToCredit(creditDto);
        credit.setCreditStatus(CreditStatus.CALCULATED);

        credit = creditRepository.save(credit);
        logger.debug("calculateCredit, save credit");

        // сохранение statement
        statement.setCreditId(credit);
        statementService.changeStatus(statement, ApplicationStatus.CC_APPROVED);
        statementService.saveStatement(statement);

        logger.debug("calculateCredit, save statement");

        String email = statement.getClientId().getEmail();
        String text = String.format(
                "Кредит одобрен.\n Перейдите по ссылке для создания документов: %s/%s/send",
                this.url, statementId.toString()
        );
        EmailMessage emailMessage = EmailMessage.builder()
                .address(email)
                .theme(Topic.CREATE_DOCUMENTS)
                .statementId(statementId)
                .text(text).build();
        producerService.sendMessage(Topic.CREATE_DOCUMENTS.getDescription(), emailMessage);
    }



}
