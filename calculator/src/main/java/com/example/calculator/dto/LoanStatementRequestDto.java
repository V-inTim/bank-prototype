package com.example.calculator.dto;

import com.example.calculator.validation.annotation.Adult;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.*;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Данные для прескоринга")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoanStatementRequestDto {
    @Schema(description = "Запрашиваемая сумма", example = "100000")
    @NotNull(message = "Сумма кредита должна быть передана.")
    @DecimalMin(value = "20000.00", message = "Сумма кредита должна быть не меньше 20000.")
    private BigDecimal amount;

    @Schema(description = "Срок", example = "12")
    @NotNull(message = "Срок кредита должен быть передан.")
    @Min(value = 6, message = "Срок кредита должен быть больше или равен 6.")
    private Integer term;

    @Schema(description = "Имя", example = "Jack")
    @NotNull(message = "Имя должно быть передано.")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Имя - от 2 до 30 латинских букв.")
    private String firstName;

    @Schema(description = "Фамилия", example = "Wain")
    @NotNull(message = "Фамилия должна быть передана.")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Фамилия - от 2 до 30 латинских букв.")
    private String lastName;

    @Schema(description = "Второе имя", example = "Jack")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Отчество - от 2 до 30 латинских букв.")
    private String middleName; // может и не быть

    @Schema(description = "Email", example = "example@test.ru")
    @NotNull(message = "Email должен быть передан.")
    @Pattern(regexp = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$",
            message = "Email должен иметь формат адреса электронной почты.")
    private String email;

    @Schema(description = "Дата рождения", example = "2001-12-12")
    @NotNull(message = "Дата рождения должна быть передана.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Adult
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "true")
    @NotNull(message = "Серия паспорта должно быть передано.")
    @Pattern(regexp = "^[0-9]{4}$", message = "Серия - 4 символа.")
    private String passportSeries;

    @Schema(description = "Номер паспорта ", example = "false")
    @NotNull(message = "Номер паспорта должно быть передано.")
    @Pattern(regexp = "^[0-9]{6}$", message = "Номер - 6 символов.")
    private String passportNumber;
}
