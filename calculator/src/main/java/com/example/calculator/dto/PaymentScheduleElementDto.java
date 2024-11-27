package com.example.calculator.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentScheduleElementDto {
    @NotNull
    Integer number;
    @NotNull
    LocalDate date;
    @NotNull
    BigDecimal totalPayment;
    @NotNull
    BigDecimal interestPayment;
    @NotNull
    BigDecimal debtPayment;
    @NotNull
    BigDecimal remainingDebt;
}
