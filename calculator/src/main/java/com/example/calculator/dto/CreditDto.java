package com.example.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Кредит")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditDto {
    @Schema(description = "Сумма кредита", example = "100000")
    @NotNull
    BigDecimal amount;

    @Schema(description = "Срок", example = "10")
    @NotNull
    Integer term;

    @Schema(description = "Ежемесячная сумма", example = "10000")
    @NotNull
    BigDecimal monthlyPayment;
    @Schema(description = "Ставка", example = "0.15")
    @NotNull
    BigDecimal rate;
    @Schema(description = "Полная сумма кредита", example = "110000")
    @NotNull
    BigDecimal psk;
    @Schema(description = "Есть ли страховка", example = "true")
    @NotNull
    Boolean isInsuranceEnabled;
    @Schema(description = "Есть ли постоянная зарплата", example = "false")
    @NotNull
    Boolean isSalaryClient;
    @Schema(description = "График")
    @NotNull
    List<PaymentScheduleElementDto> paymentSchedule;
}
