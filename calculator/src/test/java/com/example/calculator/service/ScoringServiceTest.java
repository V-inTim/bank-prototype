package com.example.calculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScoringServiceTest {
    private ScoringService scoringService;

    @BeforeEach
    public void setUp(){
        scoringService = new ScoringService();
        scoringService.standartRate = "0.05";
    }
    @Test
    public void testPreEvaluateBothFalse(){
        BigDecimal expectedRate = new BigDecimal("0.12");
        BigDecimal actualRate = scoringService.preEvaluate(false, false);
        assertEquals(expectedRate, actualRate);
    }
    @Test
    public void testPreEvaluateTrueFalse(){
        BigDecimal expectedRate = new BigDecimal("0.10");
        BigDecimal actualRate = scoringService.preEvaluate(true, false);
        assertEquals(expectedRate, actualRate);
    }
    @Test
    public void testPreEvaluateFalseTrue(){
        BigDecimal expectedRate = new BigDecimal("0.07");
        BigDecimal actualRate = scoringService.preEvaluate(false, true);
        assertEquals(expectedRate, actualRate);
    }
    @Test
    public void testPreEvaluateBothTrue(){
        BigDecimal expectedRate = new BigDecimal("0.05");
        BigDecimal actualRate = scoringService.preEvaluate(true, true);
        assertEquals(expectedRate, actualRate);
    }
}
