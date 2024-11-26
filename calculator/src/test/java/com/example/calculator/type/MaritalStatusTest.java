package com.example.calculator.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class MaritalStatusTest {

    @Test
    public void testEnumValues() {
        // Проверка наличия всех значений
        assertEquals(4, MaritalStatus.values().length);
        assertEquals(MaritalStatus.MARRIED, MaritalStatus.valueOf("MARRIED"));
        assertEquals(MaritalStatus.DIVORCED, MaritalStatus.valueOf("DIVORCED"));
        assertEquals(MaritalStatus.SINGLE, MaritalStatus.valueOf("SINGLE"));
        assertEquals(MaritalStatus.WIDOWED_WIDOWED, MaritalStatus.valueOf("WIDOWED_WIDOWED"));
    }

    @Test
    public void testValueOf() {
        // Проверка метода valueOf с корректными значениями
        assertEquals(MaritalStatus.MARRIED, MaritalStatus.valueOf("MARRIED"));
        assertEquals(MaritalStatus.DIVORCED, MaritalStatus.valueOf("DIVORCED"));
        assertEquals(MaritalStatus.SINGLE, MaritalStatus.valueOf("SINGLE"));
        assertEquals(MaritalStatus.WIDOWED_WIDOWED, MaritalStatus.valueOf("WIDOWED_WIDOWED"));
    }
}
