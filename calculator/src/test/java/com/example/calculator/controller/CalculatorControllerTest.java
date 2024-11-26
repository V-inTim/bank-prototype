package com.example.calculator.controller;

import com.example.calculator.dto.LoanOfferDto;
import com.example.calculator.dto.LoanStatementRequestDto;
import com.example.calculator.service.OfferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CalculatorControllerTest {
    @Mock
    private OfferService service;

    @InjectMocks
    private CalculatorController controller;


    private ObjectMapper objectMapper;

    @Autowired
    private  MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testSuccess() throws Exception{
        LoanStatementRequestDto dto = LoanStatementRequestDto.builder()
                .email("example@mail.ru")
                .birthdate(LocalDate.of(2002, 10, 12))
                .firstName("Tim")
                .lastName("Din")
                .middleName("Fill")
                .term(23)
                .amount(BigDecimal.valueOf(200000))
                .passportNumber("000000")
                .passportSeries("0000")
                .build();

        List<LoanOfferDto> offers = new ArrayList<>();
        offers.add(new LoanOfferDto());
        offers.add(new LoanOfferDto());
        offers.add(new LoanOfferDto());
        offers.add(new LoanOfferDto());

        lenient().when(service.generateOffers(dto)).thenReturn(offers);
        mockMvc.perform(post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void testValidationErrorEmail() throws Exception{
        LoanStatementRequestDto dto = LoanStatementRequestDto.builder()
                .email("examplemail.ru")
                .birthdate(LocalDate.of(2002, 10, 12))
                .firstName("Tim")
                .lastName("Din")
                .middleName("Fill")
                .term(23)
                .amount(BigDecimal.valueOf(1000))
                .passportNumber("000000")
                .passportSeries("0000")
                .build();

        List<LoanOfferDto> offers = new ArrayList<>();
        offers.add(new LoanOfferDto());
        offers.add(new LoanOfferDto());
        offers.add(new LoanOfferDto());
        offers.add(new LoanOfferDto());

        lenient().when(service.generateOffers(dto)).thenReturn(offers);
        mockMvc.perform(post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email")
                        .value("Email должен иметь формат адреса электронной почты."))
                .andExpect(jsonPath("$.amount")
                        .value("Сумма кредита должна быть не меньше 20000."));
    }


}
