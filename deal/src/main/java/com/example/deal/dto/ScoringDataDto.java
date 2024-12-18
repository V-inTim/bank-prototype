package com.example.deal.dto;

import com.example.deal.type.Gender;
import com.example.deal.type.MaritalStatus;
import com.example.deal.validation.annotation.EnumNamePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoringDataDto {
    @NotNull
    @Min(value = 20000, message = "Сумма кредита должна быть больше или равна 20000.")
    BigDecimal amount;

    @NotNull
    @Min(value = 6, message = "Срок кредита должен быть больше или равен 6.")
    Integer term;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Имя - от 2 до 30 латинских букв.")
    String firstName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Фамилия - от 2 до 30 латинских букв.")
    String lastName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Отчество - от 2 до 30 латинских букв.")
    String middleName; // может и не быть


    @NotNull
    @EnumNamePattern(
            regexp = "MALE|FEMALE|NON_BINARY",
            message = "Гендер один из 3 заявленных."
    )
    Gender gender;

    @NotNull
    LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "^[0-9]{4}$", message = "Серия - 4 символа.")
    String passportSeries;

    @NotNull
    @Pattern(regexp = "^[0-9]{6}$", message = "Номер - 6 символов.")
    String passportNumber;

    @NotNull
    LocalDate passportIssueDate;

    @NotNull
    String passportIssueBranch;

    @NotNull
    @EnumNamePattern(
            regexp = "MARRIED|DIVORCED|SINGLE|WIDOWED_WIDOWED",
            message = "Статус один из 4 заявленных."
    )
    MaritalStatus maritalStatus;

    @NotNull
    Integer dependentAmount;

    @NotNull
    EmploymentDto employment;

    @NotNull
    String accountNumber;

    @NotNull
    Boolean isInsuranceEnabled;

    @NotNull
    Boolean isSalaryClient;
}
