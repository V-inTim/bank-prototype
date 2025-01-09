package com.example.statement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "statementId должен быть передан.")
    @Schema(description = "Идентификатор")
    UUID statementId;
    @NotNull(message = "requestedAmount должен быть передан.")
    @Schema(description = "Запрашиваемая сумма", example = "100000")
    BigDecimal requestedAmount;
    @NotNull(message = "totalAmount должен быть передан.")
    @Schema(description = "Запрашивая сумма с доп учетом", example = "120000")
    BigDecimal totalAmount;
    @NotNull(message = "term должен быть передан.")
    @Schema(description = "Срок", example = "12")
    Integer term;
    @NotNull(message = "monthlyPayment должен быть передан.")
    @Schema(description = "Ежемесячная сумма", example = "30000")
    BigDecimal monthlyPayment;
    @NotNull(message = "rate должен быть передан.")
    @Schema(description = "Ставка", example = "0.12")
    BigDecimal rate;
    @NotNull(message = "isInsuranceEnabled должен быть передан.")
    @Schema(description = "Есть ли страховка", example = "true")
    Boolean isInsuranceEnabled;
    @NotNull(message = "isSalaryClient должен быть передан.")
    @Schema(description = "Есть ли постоянная зарплата", example = "false")
    Boolean isSalaryClient;
}
