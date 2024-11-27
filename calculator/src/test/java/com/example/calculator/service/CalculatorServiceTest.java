package com.example.calculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

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
        BigDecimal expectedTotalPayment = new BigDecimal(120000);
        BigDecimal actualTotalPayment = calculator.calculateTotalPayment(
                new BigDecimal(100000), true);

        assertEquals(expectedTotalPayment, actualTotalPayment);
    }

    @Test public void testCalculateTotalPaymentWithZeroRate() {
        BigDecimal expectedTotalPayment = new BigDecimal(240000);
        BigDecimal actualTotalPayment = calculator.calculateTotalPayment(
                new BigDecimal(200000), true);

        assertEquals(expectedTotalPayment, actualTotalPayment);
    }
}
