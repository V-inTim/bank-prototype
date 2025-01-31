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
    private BigDecimal amount;

    @Schema(description = "Срок", example = "10")
    @NotNull
    @Min(value = 6, message = "Срок кредита должен быть больше или равен 6.")
    private Integer term;

    @Schema(description = "Имя", example = "Jack")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Имя - от 2 до 30 латинских букв.")
    private String firstName;

    @Schema(description = "Фамилия", example = "Wain")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Фамилия - от 2 до 30 латинских букв.")
    private String lastName;

    @Schema(description = "Второе имя", example = "Jack")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Отчество - от 2 до 30 латинских букв.")
    private String middleName; // может и не быть

    @Schema(description = "Гендер", example = "FEMALE")
    @NotNull
    @EnumNamePattern(
            regexp = "MALE|FEMALE|NON_BINARY",
            message = "Гендер один из 3 заявленных."
    )
    private Gender gender;

    @Schema(description = "Дата рождения", example = "2001-12-12")
    @NotNull
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "2345")
    @NotNull
    @Pattern(regexp = "^[0-9]{4}$", message = "Серия - 4 символа.")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "234545")
    @NotNull
    @Pattern(regexp = "^[0-9]{6}$", message = "Номер - 6 символов.")
    private String passportNumber;

    @Schema(description = "Дата выдачи", example = "2015-12-12")
    @NotNull
    private LocalDate passportIssueDate;

    @Schema(description = "Отдел выдачи", example = "ГУУ МВД")
    @NotNull
    private String passportIssueBranch;

    @Schema(description = "Семейный статус", example = "DIVORCED")
    @NotNull
    @EnumNamePattern(
            regexp = "MARRIED|DIVORCED|SINGLE|WIDOWED_WIDOWED",
            message = "Статус один из 4 заявленных."
    )
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "3")
    @NotNull
    private Integer dependentAmount;

    @Schema(description = "Занятость")
    @NotNull
    private EmploymentDto employment;

    @Schema(description = "Личный номер", example = "8920435436")
    @NotNull
    private String accountNumber;

    @Schema(description = "Есть ли страховка", example = "true")
    @NotNull
    private Boolean isInsuranceEnabled;
    @Schema(description = "Зарплатный ли клиент", example = "false")
    @NotNull
    private Boolean isSalaryClient;
}
