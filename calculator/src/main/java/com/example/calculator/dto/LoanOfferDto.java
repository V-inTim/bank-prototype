package com.example.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Предложение")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanOfferDto {
    @Schema(description = "Идентификатор")
    UUID statementId;
    @Schema(description = "Запрашиваемая сумма", example = "100000")
    BigDecimal requestedAmount;
    @Schema(description = "Запрашивая сумма с доп учетом", example = "120000")
    BigDecimal totalAmount;
    @Schema(description = "Срок", example = "12")
    Integer term;
    @Schema(description = "Ежемесячная сумма", example = "30000")
    BigDecimal monthlyPayment;
    @Schema(description = "Ставка", example = "0.12")
    BigDecimal rate;
    @Schema(description = "Есть ли страховка", example = "true")
    Boolean isInsuranceEnabled;
    @Schema(description = "Есть ли постоянная зарплата", example = "false")
    Boolean isSalaryClient;
}

