package com.example.calculator.service;

import com.example.calculator.dto.PaymentScheduleElementDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculatorService {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorService.class);

    public BigDecimal calculateTotalPayment(BigDecimal amount, Boolean isInsuranceEnabled) {
        if (!isInsuranceEnabled) {
            return amount;
        }
        if (amount.compareTo(new BigDecimal(200000)) < 0){
            amount = amount.add(new BigDecimal(20000));
        } else {
            amount = amount.add(new BigDecimal(40000));
        }
        logger.debug("calculateTotalPayment, {}", amount);
        return amount;
    }

    public BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, int termInMonths) {
        BigDecimal one = BigDecimal.ONE;
        BigDecimal x = rate.add(one).pow(termInMonths);
        BigDecimal numerator = x.multiply(rate).multiply(amount);
        BigDecimal denominator = x.subtract(one);
        BigDecimal result =  numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        logger.debug("calculateMonthlyPayment, {}", result);
        return result;
    }

    public BigDecimal calculatePsk(BigDecimal monthlyPayment, int termInMonths) {
        BigDecimal psk = monthlyPayment.multiply(new BigDecimal(termInMonths));
        logger.debug("calculatePsk, {}", psk);
        return psk;
    }

    public List<PaymentScheduleElementDto> calculatePaymentSchedule(
            BigDecimal totalPayment, BigDecimal rate, BigDecimal psk, int term) {
        List<PaymentScheduleElementDto> schedule = new ArrayList<>();
        BigDecimal interestPayment, debtPayment, balance = psk;

        for (int i=1; i < term + 1; i++){

            interestPayment = totalPayment.multiply(rate);
            debtPayment = totalPayment.subtract(interestPayment);
            balance = balance.subtract(totalPayment);
            LocalDate date = LocalDate.now().plusMonths(i);

            schedule.add(
                    PaymentScheduleElementDto.builder()
                            .number(i)
                            .date(date)
                            .totalPayment(totalPayment)
                            .interestPayment(interestPayment)
                            .debtPayment(debtPayment)
                            .remainingDebt(balance)
                            .build());
        }
        logger.debug("calculatePaymentSchedule, {}", schedule);
        return schedule;
    }
}
