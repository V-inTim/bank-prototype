package com.example.calculator.dto;

import com.example.calculator.validator.Adult;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.*;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoanStatementRequestDto {
    @NotNull(message = "Сумма кредита должна быть передана.")
    @DecimalMin(value = "20000.00", message = "Сумма кредита должна быть больше или равна 20000.")
    private BigDecimal amount;

    @NotNull(message = "Срок кредита должен быть передан.")
    @Min(value = 6, message = "Срок кредита должен быть больше или равен 6.")
    private Integer term;

    @NotNull(message = "Имя должно быть передано.")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Имя - от 2 до 30 латинских букв.")
    private String firstName;

    @NotNull(message = "Фамилия должна быть передана.")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Фамилия - от 2 до 30 латинских букв.")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Отчество - от 2 до 30 латинских букв.")
    private String middleName; // может и не быть

    @NotNull(message = "Email должен быть передан.")
    @Email(message = "Email должен иметь формат адреса электронной почты.")
    private String email;

    @NotNull(message = "Дата рождения должна быть передана.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Adult
    private LocalDate birthdate;

    @NotNull(message = "Серия паспорта должно быть передано.")
    @Pattern(regexp = "^[0-9]{4}$", message = "Серия - 4 символа.")
    private String passportSeries;

    @NotNull(message = "Номер паспорта должно быть передано.")
    @Pattern(regexp = "^[0-9]{6}$", message = "Номер - 6 символов.")
    private String passportNumber;
}
