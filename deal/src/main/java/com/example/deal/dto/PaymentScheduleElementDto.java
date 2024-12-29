package com.example.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Элемент графика")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentScheduleElementDto {
    @Schema(description = "Номер платежа", example = "1")
    @NotNull
    Integer number;

    @Schema(description = "Дата платежа", example = "2024-12-18")
    @NotNull
    LocalDate date;

    @Schema(description = "Сумма платежа", example = "2000")
    @NotNull
    BigDecimal totalPayment;

    @Schema(description = "Процентная часть", example = "400")
    @NotNull
    BigDecimal interestPayment;

    @Schema(description = "Часть от суммы", example = "1600")
    @NotNull
    BigDecimal debtPayment;

    @Schema(description = "Оставшаяся", example = "21000")
    @NotNull
    BigDecimal remainingDebt;
}
