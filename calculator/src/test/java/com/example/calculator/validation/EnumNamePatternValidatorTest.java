package com.example.calculator.validation;

import com.example.calculator.type.Gender;
import com.example.calculator.type.MaritalStatus;
import com.example.calculator.validation.annotation.EnumNamePattern;
import com.example.calculator.validation.validator.EnumNamePatternValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnumNamePatternValidatorTest {
    private EnumNamePatternValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        validator = new EnumNamePatternValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test public void testValidEnumValue() {
        EnumNamePattern annotation = mock(EnumNamePattern.class);
        when(annotation.regexp()).thenReturn("MALE|FEMALE|NON_BINARY");
        validator.initialize(annotation);
        assertTrue(validator.isValid(Gender.MALE, context));
        assertTrue(validator.isValid(Gender.FEMALE, context));
    }

    @Test public void testInvalidEnumValue() {
        EnumNamePattern annotation = mock(EnumNamePattern.class);
        when(annotation.regexp()).thenReturn("MALE|FEMALE|NON_BINARY");
        validator.initialize(annotation);
        assertFalse(validator.isValid(MaritalStatus.MARRIED, context));
    }

    @Test
    public void testNullEnumValue() {
        EnumNamePattern annotation = mock(EnumNamePattern.class);
        when(annotation.regexp()).thenReturn("");
        validator.initialize(annotation);
        assertTrue(validator.isValid(null, context));
    }
}
