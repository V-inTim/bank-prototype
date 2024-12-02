package com.example.calculator.dto;

import com.example.calculator.type.EmploymentStatus;
import com.example.calculator.type.EmploymentPosition;
import com.example.calculator.type.MaritalStatus;
import com.example.calculator.validation.annotation.EnumNamePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Работоспособность")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDto {
    @Schema(description = "Статус занятости", example = "SELF_EMPLOYED")
    @NotNull
    @EnumNamePattern(
            regexp = "UNEMPLOYED|SELF_EMPLOYED|BUSINESS_OWNER|EMPLOYED",
            message = "Статус должен быть один из заявленных."
    )
    EmploymentStatus employmentStatus;
    @Schema(description = "ИНН", example = "234644576")
    @NotNull
    String employerINN;
    @Schema(description = "Зарплата", example = "32454")
    @NotNull
    @DecimalMin(value = "0.00", message = "Сумма кредита должна быть отрицательной.")
    BigDecimal salary;
    @Schema(description = "Позиция занятости", example = "MID_MANAGER")
    @NotNull
    @EnumNamePattern(
            regexp = "WORKER|MID_MANAGER|TOP_MANAGER|OWNER",
            message = "Позиция должна быть одна из заявленных."
    )
    EmploymentPosition position;
    @Schema(description = "Есть ли страховка", example = "true")
    @NotNull
    Integer workExperienceTotal;
    @Schema(description = "Есть ли постоянная зарплата", example = "false")
    @NotNull
    Integer workExperienceCurrent;
}
