package com.example.calculator.dto;

import com.example.calculator.type.EmploymentStatus;
import com.example.calculator.type.EmploymentPosition;
import com.example.calculator.type.MaritalStatus;
import com.example.calculator.validation.annotation.EnumNamePattern;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploymentDto {
    @NotNull
    @EnumNamePattern(
            regexp = "UNEMPLOYED|SELF_EMPLOYED|BUSINESS_OWNER|EMPLOYED",
            message = "Статус должен быть один из заявленных."
    )
    EmploymentStatus employmentStatus;
    @NotNull
    String employerINN;
    @NotNull
    @DecimalMin(value = "0.00", message = "Сумма кредита должна быть отрицательной.")
    BigDecimal salary;
    @NotNull
    @EnumNamePattern(
            regexp = "WORKER|MID_MANAGER|TOP_MANAGER|OWNER",
            message = "Позиция должна быть одна из заявленных."
    )
    EmploymentPosition position;
    @NotNull
    Integer workExperienceTotal;
    @NotNull
    Integer workExperienceCurrent;
}
