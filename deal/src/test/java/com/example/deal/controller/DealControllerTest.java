package com.example.deal.controller;

import com.example.deal.dto.LoanOfferDto;
import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.service.DealService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class DealControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private DealService dealService;

    @InjectMocks
    private DealController dealController;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testSuccessCreateStatement() {
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

        ResponseEntity<?> responseEntity =  dealController.createStatement(dto);

        verify(dealService).createStatement(any(LoanStatementRequestDto.class));
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(loanOffers, responseEntity.getBody());
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
}
