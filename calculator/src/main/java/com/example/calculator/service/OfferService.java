package com.example.calculator.service;

import com.example.calculator.dto.CreditDto;
import com.example.calculator.dto.LoanOfferDto;
import com.example.calculator.dto.LoanStatementRequestDto;
import com.example.calculator.dto.ScoringDataDto;
import com.example.calculator.dto.PaymentScheduleElementDto;
import com.example.calculator.exception.ScoringException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class OfferService {
    ScoringService scoringService;
    CalculatorService calculatorService;
    private static final Logger logger = LoggerFactory.getLogger(OfferService.class);

    @Autowired
    public OfferService(ScoringService scoringService, CalculatorService calculatorService) {

        this.scoringService = scoringService;
        this.calculatorService = calculatorService;
    }

    public List<LoanOfferDto> generateOffers(LoanStatementRequestDto requestData) {
        List<LoanOfferDto> offers = new ArrayList<>();

        offers.add(createOffer(requestData, true, true));
        offers.add(createOffer(requestData, true, false));
        offers.add(createOffer(requestData, false, true));
        offers.add(createOffer(requestData, false, false));

        offers.sort(Comparator.comparing(LoanOfferDto::getRate).reversed());
        logger.debug("Сгенерированный List<LoanOfferDto>: {}", offers);

        return offers;
    }

    private LoanOfferDto createOffer(LoanStatementRequestDto requestData,
                                     boolean isInsuranceEnabled,
                                     boolean isSalaryClient) {
        UUID uuid = UUID.randomUUID();

        BigDecimal rate = scoringService.preEvaluate(isInsuranceEnabled, isSalaryClient);
        BigDecimal totalAmount = calculatorService.calculateTotalPayment(requestData.getAmount(), isInsuranceEnabled);
        BigDecimal monthlyPayment = calculatorService.calculateMonthlyPayment(totalAmount, rate, requestData.getTerm());

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

    public CreditDto calculateCredit(ScoringDataDto requestData) throws ScoringException {
        BigDecimal rate = scoringService.evaluate(requestData);
        BigDecimal totalAmount = calculatorService.calculateTotalPayment(
                requestData.getAmount(), requestData.getIsInsuranceEnabled()
        );
        BigDecimal monthlyPayment = calculatorService.calculateMonthlyPayment(totalAmount, rate, requestData.getTerm());
        BigDecimal psk = calculatorService.calculatePsk(monthlyPayment, requestData.getTerm());
        List<PaymentScheduleElementDto> schedule = calculatorService.calculatePaymentSchedule(
                monthlyPayment, rate, psk, requestData.getTerm()
        );

        return CreditDto.builder()
                .psk(psk)
                .amount(totalAmount)
                .term(requestData.getTerm())
                .rate(rate)
                .monthlyPayment(monthlyPayment)
                .isInsuranceEnabled(requestData.getIsInsuranceEnabled())
                .isSalaryClient(requestData.getIsSalaryClient())
                .paymentSchedule(schedule)
                .build();
    }
}
