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
                12,
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
}
