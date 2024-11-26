package com.example.calculator.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService {

    public BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, int termInMonths) {
        BigDecimal one = BigDecimal.ONE;
        BigDecimal x = rate.add(one).pow(termInMonths);
        BigDecimal numerator = x.multiply(rate).multiply(amount);
        BigDecimal denominator = x.subtract(one);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
    public BigDecimal calculateTotalPayment(BigDecimal monthlyPayment, int termInMonths) {
        BigDecimal term = new BigDecimal(termInMonths);
        return monthlyPayment.multiply(term);
    }
}
