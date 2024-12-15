package com.example.calculator.type;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GenderTest {

    @Test
    public void testEnumValues() {
        // Проверка наличия всех значений
        assertEquals(3, Gender.values().length);
        assertEquals(Gender.MALE, Gender.valueOf("MALE"));
        assertEquals(Gender.FEMALE, Gender.valueOf("FEMALE"));
        assertEquals(Gender.NON_BINARY, Gender.valueOf("NON_BINARY"));
    }

    @Test
    public void testValueOf() {
        // Проверка метода valueOf с корректными значениями
        assertEquals(Gender.MALE, Gender.valueOf("MALE"));
        assertEquals(Gender.FEMALE, Gender.valueOf("FEMALE"));
        assertEquals(Gender.NON_BINARY, Gender.valueOf("NON_BINARY"));
    }
}

