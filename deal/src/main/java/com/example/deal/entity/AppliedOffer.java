package com.example.deal.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppliedOffer {

    BigDecimal requestedAmount;

    BigDecimal totalAmount;

    Integer term;

    BigDecimal monthlyPayment;

    BigDecimal rate;

    Boolean isInsuranceEnabled;

    Boolean isSalaryClient;
}
