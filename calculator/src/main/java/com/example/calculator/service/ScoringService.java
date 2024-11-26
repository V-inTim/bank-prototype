package com.example.calculator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ScoringService {
    @Value("${app.rate}")
    String standartRate;

    public BigDecimal preEvaluate(boolean isInsuranceEnabled,
                                  boolean isSalaryClient){
        BigDecimal rate = new BigDecimal(standartRate);
        if (!isInsuranceEnabled && !isSalaryClient){
            rate = rate.add(new BigDecimal("0.07"));
        } else if (!isInsuranceEnabled){
            rate = rate.add(new BigDecimal("0.02"));
        }else if (!isSalaryClient){
            rate = rate.add(new BigDecimal("0.05"));
        }

        return rate;
    }
}
