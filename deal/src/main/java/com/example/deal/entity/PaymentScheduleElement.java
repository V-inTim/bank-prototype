package com.example.deal.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentScheduleElement {

    Integer number;

    LocalDate date;

    BigDecimal totalPayment;

    BigDecimal interestPayment;

    BigDecimal debtPayment;

    BigDecimal remainingDebt;
}
