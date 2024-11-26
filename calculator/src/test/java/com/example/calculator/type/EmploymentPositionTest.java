package com.example.calculator.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmploymentPositionTest {
    @Test
    public void testEnumValues() {
        assertEquals(4, EmploymentPosition.values().length);
        assertEquals(EmploymentPosition.WORKER, EmploymentPosition.valueOf("WORKER"));
        assertEquals(EmploymentPosition.MID_MANAGER, EmploymentPosition.valueOf("MID_MANAGER"));
        assertEquals(EmploymentPosition.TOP_MANAGER, EmploymentPosition.valueOf("TOP_MANAGER"));
        assertEquals(EmploymentPosition.OWNER, EmploymentPosition.valueOf("OWNER"));
    }

    @Test
    public void testValueOf() {
        assertEquals(EmploymentPosition.WORKER, EmploymentPosition.valueOf("WORKER"));
        assertEquals(EmploymentPosition.MID_MANAGER, EmploymentPosition.valueOf("MID_MANAGER"));
        assertEquals(EmploymentPosition.TOP_MANAGER, EmploymentPosition.valueOf("TOP_MANAGER"));
        assertEquals(EmploymentPosition.OWNER, EmploymentPosition.valueOf("OWNER"));
    }
}