package com.example.calculator.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
}
