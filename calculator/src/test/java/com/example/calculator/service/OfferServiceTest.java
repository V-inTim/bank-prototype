package com.example.calculator.service;

import com.example.calculator.dto.*;
import com.example.calculator.exception.ScoringException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OfferServiceTest {
    @Mock
    private ScoringService scoringService;
    @Mock
    private CalculatorService calculatorService;
    @InjectMocks
    private OfferService offerService;
    @Mock
    private ScoringDataDto scoringDataMock;
    @Mock
    private List<PaymentScheduleElementDto> paymentScheduleMock;
    private LoanStatementRequestDto requestData;

    private BigDecimal rate;
    private BigDecimal totalAmount;
    private BigDecimal monthlyPayment;
    private BigDecimal psk;

    @BeforeEach void setUp(){
        requestData = new LoanStatementRequestDto(
                new BigDecimal("100000"),
                12,
                "John",
                "Doe",
                "Smith",
                "john.doe@example.com",
                LocalDate.of(1990, 1, 1),
                "1234",
                "567890"
        );

        rate = new BigDecimal("0.05");
        totalAmount = new BigDecimal("12000");
        monthlyPayment = new BigDecimal("1000");
        psk = new BigDecimal("13000");
        when(scoringService.evaluate(scoringDataMock)).thenReturn(rate);
        when(calculatorService.calculateTotalPayment(any(BigDecimal.class), anyBoolean())).thenReturn(totalAmount);
        when(calculatorService.calculateMonthlyPayment(any(BigDecimal.class), any(BigDecimal.class), anyInt()))
                .thenReturn(monthlyPayment);
        when(calculatorService.calculatePsk(any(BigDecimal.class), anyInt())).thenReturn(psk);
        when(calculatorService.calculatePaymentSchedule(any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class), anyInt()))
                .thenReturn(paymentScheduleMock);
    }

    @Test
    public void testGenerateOffers(){
        when(scoringService.preEvaluate(true, true))
                .thenReturn(new BigDecimal("0.05"));
        when(scoringService.preEvaluate(true, false))
                .thenReturn(new BigDecimal("0.10"));
        when(scoringService.preEvaluate(false, true))
                .thenReturn(new BigDecimal("0.08"));
        when(scoringService.preEvaluate(false, false))
                .thenReturn(new BigDecimal("0.15"));

        when(calculatorService.calculateTotalPayment(new BigDecimal("100000"), true))
                .thenReturn(new BigDecimal("120000"));
        when(calculatorService.calculateTotalPayment(new BigDecimal("100000"), false))
                .thenReturn(new BigDecimal("100000"));


        when(calculatorService.calculateMonthlyPayment(new BigDecimal("120000"), new BigDecimal("0.05"), 60))
                .thenReturn(new BigDecimal("13539.05"));
        when(calculatorService.calculateMonthlyPayment(new BigDecimal("120000"), new BigDecimal("0.10"), 60))
                .thenReturn(new BigDecimal("17611.60"));
        when(calculatorService.calculateMonthlyPayment(new BigDecimal("100000"), new BigDecimal("0.08"), 60))
                .thenReturn(new BigDecimal("12590.20"));
        when(calculatorService.calculateMonthlyPayment(new BigDecimal("100000"), new BigDecimal("0.15"), 60))
                .thenReturn(new BigDecimal("16143.63"));



        List<LoanOfferDto> offers = offerService.generateOffers(requestData);

        assertEquals(4, offers.size());
        assertEquals(new BigDecimal("13539.05"),
                calculatorService.calculateMonthlyPayment(new BigDecimal("120000"), new BigDecimal("0.05"), 60));
        assertEquals(new BigDecimal("17611.60"),
                calculatorService.calculateMonthlyPayment(new BigDecimal("120000"), new BigDecimal("0.10"), 60));
        assertEquals(new BigDecimal("12590.20"),
                calculatorService.calculateMonthlyPayment(new BigDecimal("100000"), new BigDecimal("0.08"), 60));
        assertEquals(new BigDecimal("16143.63"),
                calculatorService.calculateMonthlyPayment(new BigDecimal("100000"), new BigDecimal("0.15"), 60));
    }

    @Test
    public void testCalculateCredit() throws ScoringException {
        when(scoringDataMock.getAmount()).thenReturn(new BigDecimal("10000"));
        when(scoringDataMock.getTerm()).thenReturn(12);
        when(scoringDataMock.getIsInsuranceEnabled()).thenReturn(true);
        when(scoringDataMock.getIsSalaryClient()).thenReturn(false);

        CreditDto creditDto = offerService.calculateCredit(scoringDataMock);
        assertNotNull(creditDto);
        assertEquals(psk, creditDto.getPsk());
        assertEquals(totalAmount, creditDto.getAmount());
        assertEquals(12, creditDto.getTerm());
        assertEquals(rate, creditDto.getRate());
        assertEquals(monthlyPayment, creditDto.getMonthlyPayment());
        assertTrue(creditDto.getIsInsuranceEnabled());
        assertFalse(creditDto.getIsSalaryClient());
        assertEquals(paymentScheduleMock, creditDto.getPaymentSchedule());
    }
    @Test
    public void testCalculateCreditWithoutInsurance() throws ScoringException {
        when(scoringDataMock.getAmount()).thenReturn(new BigDecimal("10000"));
        when(scoringDataMock.getTerm()).thenReturn(24);
        when(scoringDataMock.getIsInsuranceEnabled()).thenReturn(false);
        when(scoringDataMock.getIsSalaryClient()).thenReturn(true);

        CreditDto creditDto = offerService.calculateCredit(scoringDataMock);
        assertNotNull(creditDto);
        assertEquals(psk, creditDto.getPsk());
        assertEquals(totalAmount, creditDto.getAmount());
        assertEquals(24, creditDto.getTerm());
        assertEquals(rate, creditDto.getRate());
        assertEquals(monthlyPayment, creditDto.getMonthlyPayment());
        assertFalse(creditDto.getIsInsuranceEnabled());
        assertTrue(creditDto.getIsSalaryClient());
        assertEquals(paymentScheduleMock, creditDto.getPaymentSchedule());
    }
}
