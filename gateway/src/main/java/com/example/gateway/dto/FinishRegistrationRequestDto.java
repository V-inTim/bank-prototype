package com.example.gateway.dto;

import com.example.gateway.type.Gender;
import com.example.gateway.type.MaritalStatus;
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
    private Gender gender;

    @Schema(description = "Семейный статус")
    private MaritalStatus maritalStatus;

    @Schema(description = "Дополнительная сумма")
    private Integer dependentAmount;

    @Schema(description = "Дата выдачи")
    private LocalDate passportIssueDate;

    @Schema(description = "Отдел выдачи")
    private String passportIssueBranch;

    @Schema(description = "Занятость")
    private EmploymentDto employment;

    @Schema(description = "Личный номер")
    private String accountNumber;
}
