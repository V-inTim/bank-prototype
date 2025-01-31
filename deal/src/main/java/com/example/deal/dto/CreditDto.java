package com.example.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private BigDecimal amount;

    @Schema(description = "Срок", example = "10")
    @NotNull
    private Integer term;

    @Schema(description = "Ежемесячная сумма", example = "10000")
    @NotNull
    private BigDecimal monthlyPayment;
    @Schema(description = "Ставка", example = "0.15")
    @NotNull
    private BigDecimal rate;
    @Schema(description = "Полная сумма кредита", example = "110000")
    @NotNull
    private BigDecimal psk;
    @Schema(description = "Есть ли страховка", example = "true")
    @NotNull
    private Boolean isInsuranceEnabled;
    @Schema(description = "Есть ли постоянная зарплата", example = "false")
    @NotNull
    private Boolean isSalaryClient;
    @Schema(description = "График")
    @NotNull
    private List<PaymentScheduleElementDto> paymentSchedule;
}
