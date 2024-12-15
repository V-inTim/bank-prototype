package com.example.calculator.dto;

import com.example.calculator.type.Gender;
import com.example.calculator.type.MaritalStatus;
import com.example.calculator.validation.annotation.EnumNamePattern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Данные для скоринга")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoringDataDto {
    @Schema(description = "Сумма кредита", example = "100000")
    @NotNull
    @Min(value = 20000, message = "Сумма кредита должна быть больше или равна 20000.")
    BigDecimal amount;

    @Schema(description = "Срок", example = "10")
    @NotNull
    @Min(value = 6, message = "Срок кредита должен быть больше или равен 6.")
    Integer term;

    @Schema(description = "Имя", example = "Jack")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Имя - от 2 до 30 латинских букв.")
    String firstName;

    @Schema(description = "Фамилия", example = "Wain")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Фамилия - от 2 до 30 латинских букв.")
    String lastName;

    @Schema(description = "Второе имя", example = "Jack")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Отчество - от 2 до 30 латинских букв.")
    String middleName; // может и не быть

    @Schema(description = "Гендер", example = "FEMALE")
    @NotNull
    @EnumNamePattern(
            regexp = "MALE|FEMALE|NON_BINARY",
            message = "Гендер один из 3 заявленных."
    )
    Gender gender;

    @Schema(description = "Дата рождения", example = "2001-12-12")
    @NotNull
    LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "2345")
    @NotNull
    @Pattern(regexp = "^[0-9]{4}$", message = "Серия - 4 символа.")
    String passportSeries;

    @Schema(description = "Номер паспорта", example = "234545")
    @NotNull
    @Pattern(regexp = "^[0-9]{6}$", message = "Номер - 6 символов.")
    String passportNumber;

    @Schema(description = "Дата выдачи", example = "2015-12-12")
    @NotNull
    LocalDate passportIssueDate;

    @Schema(description = "Отдел выдачи", example = "ГУУ МВД")
    @NotNull
    String passportIssueBranch;

    @Schema(description = "Семейный статус", example = "DIVORCED")
    @NotNull
    @EnumNamePattern(
            regexp = "MARRIED|DIVORCED|SINGLE|WIDOWED_WIDOWED",
            message = "Статус один из 4 заявленных."
    )
    MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "3")
    @NotNull
    Integer dependentAmount;

    @Schema(description = "Занятость")
    @NotNull
    EmploymentDto employment;

    @Schema(description = "Личный номер", example = "8920435436")
    @NotNull
    String accountNumber;

    @Schema(description = "Есть ли страховка", example = "true")
    @NotNull
    Boolean isInsuranceEnabled;
    @Schema(description = "Зарплатный ли клиент", example = "false")
    @NotNull
    Boolean isSalaryClient;
}
