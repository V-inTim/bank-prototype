package com.example.gateway.dto;


import com.example.gateway.types.EmploymentPosition;
import com.example.gateway.types.EmploymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmploymentDto {
    @Schema(description = "Статус занятости", example = "SELF_EMPLOYED")
    EmploymentStatus employmentStatus;

    @Schema(description = "ИНН", example = "234644576")
    String employerINN;

    @Schema(description = "Зарплата", example = "32454")
    BigDecimal salary;

    @Schema(description = "Позиция занятости", example = "MID_MANAGER")
    EmploymentPosition position;

    @Schema(description = "Рабочий опыт общий", example = "24")
    Integer workExperienceTotal;

    @Schema(description = "Рабочий опыт текущий", example = "20")
    Integer workExperienceCurrent;
}
