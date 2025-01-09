package com.example.calculator.service;

import com.example.calculator.dto.PaymentScheduleElementDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CalculatorServiceTest {

    private CalculatorService calculator;
    @BeforeEach
    public void setUp() {
        calculator = new CalculatorService();
    }

    @Test
    public void testCalculateMonthlyPayment() {
        BigDecimal amount = new BigDecimal("100000");
        BigDecimal rate = new BigDecimal("0.05");
        int termInMonths = 60;
        BigDecimal expectedMonthlyPayment = new BigDecimal("5282.82");
        // предполагаемый результат, точность до 2-х знаков
        BigDecimal actualMonthlyPayment = calculator.calculateMonthlyPayment(amount, rate, termInMonths);
        assertEquals(expectedMonthlyPayment, actualMonthlyPayment);
    }

    @Test
    public void testCalculateTotalPayment() {
        BigDecimal expectedTotalPayment = new BigDecimal(120000);
        BigDecimal actualTotalPayment = calculator.calculateTotalPayment(
                new BigDecimal(100000), true);

        assertEquals(expectedTotalPayment, actualTotalPayment);
    }

    @Test
    public void testCalculatePsk() {
        BigDecimal expectedTotalPayment = new BigDecimal(240000);
        BigDecimal actualTotalPayment = calculator.calculatePsk(
                new BigDecimal(20000), 12);

        assertEquals(expectedTotalPayment, actualTotalPayment);
    }

    @Test
    public void testCalculatePaymentSchedule() {
        BigDecimal totalPayment = new BigDecimal("1000");
        BigDecimal rate = new BigDecimal("50");
        BigDecimal psk = new BigDecimal("10000");
        int term = 12;
        List<PaymentScheduleElementDto> schedule = calculator.calculatePaymentSchedule(totalPayment, rate, psk, term);

        assertEquals(12, schedule.size());
        assertEquals(1, schedule.get(0).getNumber());
        assertEquals(LocalDate.now().plusMonths(1), schedule.get(0).getDate());
        assertEquals(totalPayment, schedule.get(0).getTotalPayment());
        assertEquals(totalPayment.multiply(rate), schedule.get(0).getInterestPayment());

    }

}
