package com.example.calculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorServiceTest {

    private CalculatorService calculator;
    @BeforeEach
    public void setUp() {
        calculator = new CalculatorService();
    }

    @Test public void testCalculateMonthlyPayment() {
        BigDecimal amount = new BigDecimal("100000");
        BigDecimal rate = new BigDecimal("0.05");
        int termInMonths = 60;
        BigDecimal expectedMonthlyPayment = new BigDecimal("5282.82");
        // предполагаемый результат, точность до 2-х знаков
        BigDecimal actualMonthlyPayment = calculator.calculateMonthlyPayment(amount, rate, termInMonths);
        assertEquals(expectedMonthlyPayment, actualMonthlyPayment);
    }

    @Test public void testCalculateTotalPayment() {
        BigDecimal monthlyPayment = new BigDecimal("5282.82");
        int termInMonths = 60;
        BigDecimal expectedTotalPayment = new BigDecimal("316969.20");
        BigDecimal actualTotalPayment = calculator.calculateTotalPayment(monthlyPayment, termInMonths);
        assertEquals(expectedTotalPayment, actualTotalPayment);
    }

    @Test public void testCalculateTotalPaymentWithZeroRate() {
        BigDecimal monthlyPayment = new BigDecimal("1666.67");
        int termInMonths = 60;
        BigDecimal expectedTotalPayment = new BigDecimal("100000.20");
        // с округлением до 2-х знаков
        BigDecimal actualTotalPayment = calculator.calculateTotalPayment(monthlyPayment, termInMonths);
        assertEquals(expectedTotalPayment, actualTotalPayment);
    }
}
