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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DealServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private StatementService statementService;
    @Mock
    private CreditRepository creditRepository;

    @Mock
    private ClientMapper clientMapper;
    @Mock
    private OfferMapper offerMapper;
    @Mock
    private CreditMapper creditMapper;

    @Mock
    private CalculatorClient calculatorClient;

    @Mock
    private  KafkaProducerService producerService;

    @InjectMocks
    private DealService dealService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSuccessCreateStatement() {
        LoanStatementRequestDto requestDto = LoanStatementRequestDto.builder()
                .email("test@example.com")
                .birthdate(LocalDate.of(1990, 1, 1))
                .firstName("John")
                .lastName("Doe")
                .middleName("Middle")
                .term(12)
                .amount(BigDecimal.valueOf(50000))
                .passportNumber("123456")
                .passportSeries("1234")
                .build();

        Client client = Client.builder()
                .clientId(UUID.randomUUID())
                .email(requestDto.getEmail())
                .birthDate(requestDto.getBirthdate())
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .build();

        Statement statement = Statement.builder()
                .statementId(UUID.randomUUID())
                .creationDate(LocalDateTime.now())
                .clientId(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .build();
        List<LoanOfferDto> offers = List.of(new LoanOfferDto(), new LoanOfferDto());

        when(clientMapper.dtoToClient(requestDto)).thenReturn(client);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        doNothing().when(statementService).saveStatement(any(Statement.class));
        when(calculatorClient.requestOffers(requestDto)).thenReturn(offers);


        // Вызов метода
        List<LoanOfferDto> resultOffers = dealService.createStatement(requestDto);

        // Проверка сохранения клиента
        verify(clientRepository).save(client);

        // Проверка сохранения заявления
        ArgumentCaptor<Statement> statementCaptor = ArgumentCaptor.forClass(Statement.class);
        verify(statementService).saveStatement(statementCaptor.capture());
        Statement savedStatement = statementCaptor.getValue();

        assertNotNull(savedStatement.getCreationDate());
        assertEquals(ApplicationStatus.PREAPPROVAL, savedStatement.getStatus());

        // Проверка вызова внешнего клиента
        verify(calculatorClient).requestOffers(requestDto);

        // Проверка корректности результата
        assertEquals(2, resultOffers.size());
    }

    @Test
    public void testSuccessApplyOffer() {
        UUID statementId = UUID.randomUUID();
        LoanOfferDto loanOfferDto = LoanOfferDto.builder()
                .statementId(statementId)
                .build();
        Statement statement = Statement.builder()
                .statementId(statementId)
                .clientId(new Client())
                .build();

        when(statementService.getStatement(statementId)).thenReturn(statement);
        when(offerMapper.dtoToAppliedOffer(any(LoanOfferDto.class))).thenReturn(new AppliedOffer());
        doAnswer(invocation -> {
            statement.setStatus(ApplicationStatus.APPROVED);
            return null;
        }).when(statementService).changeStatus(any(Statement.class), eq(ApplicationStatus.APPROVED));

        // Вызов метода
        dealService.applyOffer(loanOfferDto);

        ArgumentCaptor<Statement> statementCaptor = ArgumentCaptor.forClass(Statement.class);
        verify(statementService).saveStatement(statementCaptor.capture());

        Statement savedStatement = statementCaptor.getValue();

        assertEquals(ApplicationStatus.APPROVED, savedStatement.getStatus());
    }

    @Test
    public void testSuccessCalculateCredit() {
        // заготовки
        UUID statementId = UUID.randomUUID();
        FinishRegistrationRequestDto dto = FinishRegistrationRequestDto.builder().
                employment(new EmploymentDto()).build();

        Client client = Client.builder().passport(new Passport()).build();
        Statement statement = Statement.builder()
                .statementId(statementId)
                .clientId(client)
                .appliedOffer(new AppliedOffer())
                .status(ApplicationStatus.APPROVED)
                .statusHistory(new ArrayList<>())
                .build();

        Credit credit = new Credit();
        // настройки
        when(statementService.getStatement(statementId)).thenReturn(statement);
        when(calculatorClient.requestCalc(any(ScoringDataDto.class), eq(statementId))).thenReturn(new CreditDto());
        when(creditMapper.dtoToCredit(any(CreditDto.class))).thenReturn(credit);
        when(creditRepository.save(credit)).thenReturn(credit);
        doNothing().when(statementService).saveStatement(statement);
        doAnswer(invocation -> {
            statement.setStatus(ApplicationStatus.CC_APPROVED);
            return null;
        }).when(statementService).changeStatus(any(Statement.class), eq(ApplicationStatus.CC_APPROVED));


        // вызов метода
        dealService.calculateCredit(dto, statementId);

        ArgumentCaptor<Statement> statementCaptor = ArgumentCaptor.forClass(Statement.class);

        // проверка на финальном этапе

        verify(statementService).saveStatement(statementCaptor.capture());
        Statement savedStatement = statementCaptor.getValue();
        assertEquals(credit, savedStatement.getCreditId());
        assertNotNull(savedStatement.getStatusHistory());
    }
}
