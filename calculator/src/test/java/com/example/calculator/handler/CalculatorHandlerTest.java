package com.example.calculator.handler;

import com.example.calculator.controller.CalculatorController;
import com.example.calculator.dto.EmploymentDto;
import com.example.calculator.dto.ScoringDataDto;
import com.example.calculator.exception.ScoringException;
import com.example.calculator.service.OfferService;
import com.example.calculator.type.EmploymentPosition;
import com.example.calculator.type.EmploymentStatus;
import com.example.calculator.type.Gender;
import com.example.calculator.type.MaritalStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CalculatorController.class)
@Import(CalculatorHandler.class)
public class CalculatorHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OfferService offerService;

    private ScoringDataDto validData;

    @BeforeEach
    public void setUp() {
        validData = new ScoringDataDto(
                new BigDecimal("100000"),
                12,
                "Tim", "Fill", "Din",
                Gender.MALE,
                LocalDate.of(1980, 1,1),
                "0000", "000000",
                LocalDate.of(2012, 10, 12),
                "none",
                MaritalStatus.SINGLE,
                34, // "dependentAmount"
                new EmploymentDto(
                        EmploymentStatus.BUSINESS_OWNER,
                        "2134541",
                        new BigDecimal(20000), // salary
                        EmploymentPosition.TOP_MANAGER,
                        18, 12

                ),
        "accountNumber",
                true,
                false
        );
    }

    @Test
    public void testHandleScoringException() throws Exception {
        when(offerService.calculateCredit(validData)).thenThrow(new ScoringException("Test Scoring Exception"));
        mockMvc.perform(post("/calculator/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validData)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Test Scoring Exception"));
    }

}
