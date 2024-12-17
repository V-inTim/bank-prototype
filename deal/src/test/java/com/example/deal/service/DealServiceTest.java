package com.example.deal.service;

import com.example.deal.client.CalculatorClient;
import com.example.deal.dto.LoanOfferDto;
import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.entity.AppliedOffer;
import com.example.deal.entity.Client;
import com.example.deal.entity.Statement;
import com.example.deal.exception.DbException;
import com.example.deal.mapper.ClientMapper;
import com.example.deal.mapper.OfferMapper;
import com.example.deal.repository.ClientRepository;
import com.example.deal.repository.StatementRepository;
import com.example.deal.type.ApplicationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DealServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private StatementRepository statementRepository;

    @Mock
    private ClientMapper clientMapper;
    @Mock
    private OfferMapper offerMapper;

    @Mock
    private CalculatorClient calculatorClient;

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
        when(statementRepository.save(any(Statement.class))).thenReturn(statement);
        when(calculatorClient.requestOffers(requestDto)).thenReturn(offers);


        // Вызов метода
        List<LoanOfferDto> resultOffers = dealService.createStatement(requestDto);

        // Проверка сохранения клиента
        verify(clientRepository).save(client);

        // Проверка сохранения заявления
        ArgumentCaptor<Statement> statementCaptor = ArgumentCaptor.forClass(Statement.class);
        verify(statementRepository).save(statementCaptor.capture());
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
                .build();

        when(statementRepository.findById(statementId)).thenReturn(Optional.of(statement));
        when(offerMapper.dtoToAppliedOffer(any(LoanOfferDto.class))).thenReturn(new AppliedOffer());

        // Вызов метода
        dealService.applyOffer(loanOfferDto);

        ArgumentCaptor<Statement> statementCaptor = ArgumentCaptor.forClass(Statement.class);
        verify(statementRepository).save(statementCaptor.capture());

        Statement savedStatement = statementCaptor.getValue();
        assertNotNull(savedStatement.getStatusHistory());
        assertEquals(ApplicationStatus.APPROVED, savedStatement.getStatus());
    }
    @Test
    void testThrowsDbException() {
        // Данные для теста
        UUID statementId = UUID.randomUUID();
        LoanOfferDto loanOfferDto = LoanOfferDto.builder()
                .statementId(statementId)
                .build();

        // Мокируем отсутствие записи в базе
        when(statementRepository.findById(statementId)).thenReturn(Optional.empty());

        // Проверяем, что выбрасывается исключение
        DbException exception = assertThrows(DbException.class, () -> dealService.applyOffer(loanOfferDto));
        assertEquals("Resource with the given ID does not exist.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

    }
}
