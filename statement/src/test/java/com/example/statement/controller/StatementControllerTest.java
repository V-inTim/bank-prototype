package com.example.statement.controller;

import com.example.statement.dto.LoanOfferDto;
import com.example.statement.dto.LoanStatementRequestDto;
import com.example.statement.service.StatementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@AutoConfigureMockMvc
@WebMvcTest(StatementController.class)
@ExtendWith(MockitoExtension.class)
public class StatementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatementService dealService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testSuccessCreateStatement() throws Exception {
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
        List<LoanOfferDto> loanOffers = List.of(new LoanOfferDto());
        when(dealService.createStatement(dto)).thenReturn(loanOffers);

        mockMvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(1));

    }

    @Test
    public void testValidationErrorEmail() throws Exception {
        LoanStatementRequestDto dto = LoanStatementRequestDto.builder()
                .email("examplemail.ru")
                .birthdate(LocalDate.of(2002, 10, 12))
                .firstName("Tim")
                .lastName("Din")
                .middleName("Fill")
                .term(23)
                .amount(BigDecimal.valueOf(200000))
                .passportNumber("000000")
                .passportSeries("0000")
                .build();

        mockMvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())  // Проверяем статус 400
                .andExpect(jsonPath("$.email").value("Email должен иметь формат адреса электронной почты."));  // Проверяем ошибку в email
    }

    @Test
    public void testSuccessApplyOffer() throws Exception{
        LoanOfferDto dto = LoanOfferDto.builder()
                .statementId(UUID.randomUUID())
                .rate(new BigDecimal("0.01"))
                .monthlyPayment(new BigDecimal(12000))
                .totalAmount(new BigDecimal(120000))
                .requestedAmount(new BigDecimal(100000))
                .term(12)
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build();
        doNothing().when(dealService).applyOffer(any(LoanOfferDto.class));

        mockMvc.perform(post("/statement/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testMissingArgument() throws Exception{
        LoanOfferDto dto = LoanOfferDto.builder()
                .statementId(UUID.randomUUID())
                .monthlyPayment(new BigDecimal(12000))
                .totalAmount(new BigDecimal(120000))
                .requestedAmount(new BigDecimal(100000))
                .term(12)
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build();

        mockMvc.perform(post("/statement/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.rate").value("rate должен быть передан."));  // Проверяем ошибку в email

    }

}
