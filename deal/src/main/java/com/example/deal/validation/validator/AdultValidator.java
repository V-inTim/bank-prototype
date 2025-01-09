package com.example.deal.validation.validator;

import com.example.deal.validation.annotation.Adult;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {
    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext constraintValidatorContext) {
        if (birthdate == null) {
            return true; // Исходя из логики, что, если нужно проверить на Null, используй @NotNull
        }
        LocalDate now = LocalDate.now();
        Period age = Period.between(birthdate, now);
        return age.getYears() >= 18;
    }
}
