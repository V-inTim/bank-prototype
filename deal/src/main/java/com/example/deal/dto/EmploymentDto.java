package com.example.deal.dto;

import com.example.deal.type.EmploymentPosition;
import com.example.deal.type.EmploymentStatus;
import com.example.deal.validation.annotation.EnumNamePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmploymentDto {
    @Schema(description = "Статус занятости", example = "SELF_EMPLOYED")
    @NotNull(message = "employmentStatus должен быть передан.")
    @EnumNamePattern(
            regexp = "UNEMPLOYED|SELF_EMPLOYED|BUSINESS_OWNER|EMPLOYED",
            message = "Статус должен быть один из заявленных."
    )
    EmploymentStatus employmentStatus;

    @Schema(description = "ИНН", example = "234644576")
    @NotNull(message = "employerINN должен быть передан.")
    String employerINN;

    @Schema(description = "Зарплата", example = "32454")
    @NotNull(message = "salary должен быть передан.")
    @DecimalMin(value = "0.00", message = "Сумма кредита должна быть отрицательной.")
    BigDecimal salary;

    @Schema(description = "Позиция занятости", example = "MID_MANAGER")
    @NotNull(message = "position должен быть передан.")
    @EnumNamePattern(
            regexp = "WORKER|MID_MANAGER|TOP_MANAGER|OWNER",
            message = "Позиция должна быть одна из заявленных."
    )
    EmploymentPosition position;

    @Schema(description = "Рабочий опыт общий", example = "24")
    @NotNull(message = "workExperienceTotal должен быть передан.")
    Integer workExperienceTotal;

    @Schema(description = "Рабочий опыт текущий", example = "20")
    @NotNull(message = "workExperienceCurrent должен быть передан.")
    Integer workExperienceCurrent;
}
