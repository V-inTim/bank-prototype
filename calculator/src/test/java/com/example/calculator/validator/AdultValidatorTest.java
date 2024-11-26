package com.example.calculator.validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class AdultValidatorTest {

    private AdultValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        validator = new AdultValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    public void testIsValidWhenBirthdateIsNull() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    public void testIsValidWhenAgeIsEighteen() {
        LocalDate birthdate = LocalDate.now().minusYears(18);
        assertTrue(validator.isValid(birthdate, context));
    }

    @Test
    public void testIsValidWhenAgeIsGreaterThanEighteen() {
        LocalDate birthdate = LocalDate.now().minusYears(20);
        assertTrue(validator.isValid(birthdate, context));
    }

    @Test
    public void testIsInvalidWhenAgeIsLessThanEighteen() {
        LocalDate birthdate = LocalDate.now().minusYears(17);
        assertFalse(validator.isValid(birthdate, context));
    }
}

