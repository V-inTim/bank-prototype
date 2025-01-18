package com.example.gateway.dto;

import com.example.gateway.types.Gender;
import com.example.gateway.types.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Schema(description = "Финальные данные")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FinishRegistrationRequestDto {
    @Schema(description = "Гендер")
    Gender gender;

    @Schema(description = "Семейный статус")
    MaritalStatus maritalStatus;

    @Schema(description = "Дополнительная сумма")
    Integer dependentAmount;

    @Schema(description = "Дата выдачи")
    LocalDate passportIssueDate;

    @Schema(description = "Отдел выдачи")
    String passportIssueBranch;

    @Schema(description = "Занятость")
    EmploymentDto employment;

    @Schema(description = "Личный номер")
    String accountNumber;
}
