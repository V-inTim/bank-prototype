package com.example.calculator.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EmploymentStatusTest {

    @Test
    public void testEnumValues() {
        // Проверка наличия всех значений
        assertEquals(4, EmploymentStatus.values().length);
        assertEquals(EmploymentStatus.UNEMPLOYED, EmploymentStatus.valueOf("UNEMPLOYED"));
        assertEquals(EmploymentStatus.SELF_EMPLOYED, EmploymentStatus.valueOf("SELF_EMPLOYED"));
        assertEquals(EmploymentStatus.BUSINESS_OWNER, EmploymentStatus.valueOf("BUSINESS_OWNER"));
        assertEquals(EmploymentStatus.EMPLOYED, EmploymentStatus.valueOf("EMPLOYED"));
    }

    @Test
    public void testValueOf() {
        // Проверка метода valueOf с корректными значениями
        assertEquals(EmploymentStatus.UNEMPLOYED, EmploymentStatus.valueOf("UNEMPLOYED"));
        assertEquals(EmploymentStatus.SELF_EMPLOYED, EmploymentStatus.valueOf("SELF_EMPLOYED"));
        assertEquals(EmploymentStatus.BUSINESS_OWNER, EmploymentStatus.valueOf("BUSINESS_OWNER"));
        assertEquals(EmploymentStatus.EMPLOYED, EmploymentStatus.valueOf("EMPLOYED"));
    }

}

