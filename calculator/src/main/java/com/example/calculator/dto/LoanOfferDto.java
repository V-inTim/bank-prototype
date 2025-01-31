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
    private UUID statementId;
    @Schema(description = "Запрашиваемая сумма", example = "100000")
    private BigDecimal requestedAmount;
    @Schema(description = "Запрашивая сумма с доп учетом", example = "120000")
    private BigDecimal totalAmount;
    @Schema(description = "Срок", example = "12")
    private Integer term;
    @Schema(description = "Ежемесячная сумма", example = "30000")
    private BigDecimal monthlyPayment;
    @Schema(description = "Ставка", example = "0.12")
    private BigDecimal rate;
    @Schema(description = "Есть ли страховка", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(description = "Есть ли постоянная зарплата", example = "false")
    private Boolean isSalaryClient;
}

