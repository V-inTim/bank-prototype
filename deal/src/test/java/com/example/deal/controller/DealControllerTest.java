package com.example.deal.controller;

import com.example.deal.dto.LoanOfferDto;
import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.service.DealService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;




@AutoConfigureMockMvc
@WebMvcTest(DealController.class)
@ExtendWith(MockitoExtension.class)
public class DealControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DealService dealService;

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

        mockMvc.perform(post("/deal/statement")
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

        mockMvc.perform(post("/deal/statement")
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

        mockMvc.perform(post("/deal/offer/select")
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

        mockMvc.perform(post("/deal/offer/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.rate").value("rate должен быть передан."));  // Проверяем ошибку в email

    }
}
