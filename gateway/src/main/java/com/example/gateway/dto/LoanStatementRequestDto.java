package com.example.gateway.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Предварительные данные.")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoanStatementRequestDto {
    @Schema(description = "Запрашиваемая сумма", example = "100000")
    private BigDecimal amount;

    @Schema(description = "Срок", example = "12")
    private Integer term;

    @Schema(description = "Имя", example = "Jack")
    private String firstName;

    @Schema(description = "Фамилия", example = "Wain")
    private String lastName;

    @Schema(description = "Второе имя", example = "Jack")
    private String middleName;

    @Schema(description = "Email", example = "example@test.ru")
    private String email;

    @Schema(description = "Дата рождения", example = "2001-12-12")
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "2345")
    private String passportSeries;

    @Schema(description = "Номер паспорта ", example = "345466")
    private String passportNumber;
}