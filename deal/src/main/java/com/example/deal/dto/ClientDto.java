package com.example.deal.dto;

import com.example.deal.entity.Employment;
import com.example.deal.entity.Passport;
import com.example.deal.type.Gender;
import com.example.deal.type.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDto {
    @Schema(description = "Фамилия")
    private String lastName;

    @Schema(description = "Имя")
    private String firstName;

    @Schema(description = "Второе имя")
    private String middleName;

    @Schema(description = "Дата рождения")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Гендер")
    private Gender gender;

    @Schema(description = "Семейный статус")
    private MaritalStatus maritalStatus;

    @Schema(description = "Дополнительная сумма")
    private Integer dependentAmount;

    @Schema(description = "Пасспорт")
    private Passport passport;

    @Schema(description = "Занятость")
    private Employment employment;

    @Schema(description = "Номер")
    private String accountNumber;
}
