package com.example.calculator.service;

import com.example.calculator.dto.PaymentScheduleElementDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculatorService {

    public BigDecimal calculateTotalPayment(BigDecimal amount, Boolean isInsuranceEnabled) {
        if (!isInsuranceEnabled) {
            return amount;
        }
        if (amount.compareTo(new BigDecimal(200000)) < 0){
            return amount.add(new BigDecimal(20000));
        } else {
            return amount.add(new BigDecimal(40000));
        }
    }

    public BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, int termInMonths) {
        BigDecimal one = BigDecimal.ONE;
        BigDecimal x = rate.add(one).pow(termInMonths);
        BigDecimal numerator = x.multiply(rate).multiply(amount);
        BigDecimal denominator = x.subtract(one);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculatePsk(BigDecimal monthlyPayment, int termInMonths) {
        return monthlyPayment.multiply(new BigDecimal(termInMonths));
    }

    public List<PaymentScheduleElementDto> calculatePaymentSchedule(
            BigDecimal totalPayment, BigDecimal rate, BigDecimal psk, int term) {
        List<PaymentScheduleElementDto> schedule = new ArrayList<>();
        BigDecimal interestPayment, debtPayment, balance = psk;

        for (int i=1; i < term + 1; i++){

            interestPayment = balance.add(rate);
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
        return  schedule;
    }
}
