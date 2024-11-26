package com.example.calculator.service;

import com.example.calculator.dto.LoanOfferDto;
import com.example.calculator.dto.LoanStatementRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OfferServiceTest {
    @Mock
    private ScoringService scoringService;
    @Mock
    private CalculatorService calculatorService;
    @InjectMocks
    private OfferService offerService;
    private LoanStatementRequestDto requestData;

    @BeforeEach void setUp(){
        requestData = new LoanStatementRequestDto(
                new BigDecimal("100000"),
                60,
                "John",
                "Doe",
                "Smith",
                "john.doe@example.com",
                LocalDate.of(1990, 1, 1),
                "1234",
                "567890"
        );
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

        when(calculatorService.calculateMonthlyPayment(new BigDecimal("100000"), new BigDecimal("0.05"), 60))
                .thenReturn(new BigDecimal("1887.12"));
        when(calculatorService.calculateMonthlyPayment(new BigDecimal("100000"), new BigDecimal("0.10"), 60))
                .thenReturn(new BigDecimal("2113.56"));
        when(calculatorService.calculateMonthlyPayment(new BigDecimal("100000"), new BigDecimal("0.08"), 60))
                .thenReturn(new BigDecimal("2027.64"));
        when(calculatorService.calculateMonthlyPayment(new BigDecimal("100000"), new BigDecimal("0.15"), 60))
                .thenReturn(new BigDecimal("2334.23"));

        when(calculatorService.calculateTotalPayment(new BigDecimal("1887.12"), 60))
                .thenReturn(new BigDecimal("113227.20"));
        when(calculatorService.calculateTotalPayment(new BigDecimal("2113.56"), 60))
                .thenReturn(new BigDecimal("126813.60"));
        when(calculatorService.calculateTotalPayment(new BigDecimal("2027.64"), 60))
                .thenReturn(new BigDecimal("121658.40"));
        when(calculatorService.calculateTotalPayment(new BigDecimal("2334.23"), 60))
                .thenReturn(new BigDecimal("140053.80"));

        List<LoanOfferDto> offers = offerService.generateOffers(requestData);

        assertEquals(4, offers.size());
        assertEquals(new BigDecimal("1887.12"), offers.get(0).getMonthlyPayment());
        assertEquals(new BigDecimal("2113.56"), offers.get(1).getMonthlyPayment());
        assertEquals(new BigDecimal("2027.64"), offers.get(2).getMonthlyPayment());
        assertEquals(new BigDecimal("2334.23"), offers.get(3).getMonthlyPayment());
    }
}
