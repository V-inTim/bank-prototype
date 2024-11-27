package com.example.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditDto {
    @NotNull
    BigDecimal amount;
    @NotNull
    Integer term;
    @NotNull
    BigDecimal monthlyPayment;
    @NotNull
    BigDecimal rate;
    @NotNull
    BigDecimal psk;
    @NotNull
    Boolean isInsuranceEnabled;
    @NotNull
    Boolean isSalaryClient;
    @NotNull
    List<PaymentScheduleElementDto> paymentSchedule;
}
