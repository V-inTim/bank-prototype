package com.example.deal.dto;

import com.example.deal.type.Gender;
import com.example.deal.type.MaritalStatus;
import com.example.deal.validation.annotation.EnumNamePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FinishRegistrationRequestDto {
    @NotNull(message = "gender должен быть передан.")
    @Schema(description = "Гендер")
    @EnumNamePattern(
            regexp = "MALE|FEMALE|NON_BINARY",
            message = "Гендер должен быть один из 3."
    )
    Gender gender;

    @NotNull(message = "maritalStatus должен быть передан.")
    @Schema(description = "Семейный статус")
    @EnumNamePattern(
            regexp = "MARRIED|DIVORCED|SINGLE|WIDOWED_WIDOWED",
            message = "Статус один из 4 заявленных."
    )
    MaritalStatus maritalStatus;

    @NotNull(message = "dependentAmount должен быть передан.")
    @Schema(description = "Дополнительная сумма")
    Integer dependentAmount;

    @NotNull(message = "passportIssueDate должен быть передан.")
    @Schema(description = "Дата выдачи")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate passportIssueDate;

    @NotNull(message = "IssueBranch должен быть передан.")
    @Schema(description = "Отдел выдачи")
    String passportIssueBranch;


    @NotNull(message = "IssueBranch должен быть передан.")
    @Schema(description = "Занятость")
    EmploymentDto employment;

    @NotNull(message = "accountNumber должен быть передан.")
    @Schema(description = "Личный номер")
    String accountNumber;
}
