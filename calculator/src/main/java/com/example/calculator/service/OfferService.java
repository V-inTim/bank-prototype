package com.example.calculator.service;

import com.example.calculator.dto.LoanOfferDto;
import com.example.calculator.dto.LoanStatementRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OfferService {
    ScoringService scoringService;
    CalculatorService calculatorService;
    @Autowired
    public OfferService(ScoringService scoringService, CalculatorService calculatorService) {

        this.scoringService = scoringService;
        this.calculatorService = calculatorService;
    }

    public List<LoanOfferDto> generateOffers(LoanStatementRequestDto requestData){
        List<LoanOfferDto> offers = new ArrayList<>();

        offers.add(createOffer(requestData, true, true));
        offers.add(createOffer(requestData, true, false));
        offers.add(createOffer(requestData, false, true));
        offers.add(createOffer(requestData, false, false));

        return offers;
    }

    private LoanOfferDto createOffer(LoanStatementRequestDto requestData,
                                     boolean isInsuranceEnabled,
                                     boolean isSalaryClient){
        UUID uuid = UUID.randomUUID();

        BigDecimal rate = scoringService.preEvaluate(isInsuranceEnabled, isSalaryClient);

        BigDecimal monthlyPayment = calculatorService.calculateMonthlyPayment(
                requestData.getAmount(), rate, requestData.getTerm());
        BigDecimal totalAmount = calculatorService.calculateTotalPayment(monthlyPayment, requestData.getTerm());

        return LoanOfferDto.builder()
                .statementId(uuid)
                .requestedAmount(requestData.getAmount())
                .totalAmount(totalAmount)
                .term(requestData.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();
    }
}
