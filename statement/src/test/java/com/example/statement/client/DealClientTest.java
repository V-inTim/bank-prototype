package com.example.statement.client;

import com.example.statement.dto.LoanOfferDto;
import com.example.statement.dto.LoanStatementRequestDto;
import com.example.statement.exception.DealErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@RestClientTest(DealClient.class)
@TestPropertySource(properties = "deal.url=base_url")
public class DealClientTest {
    @Autowired
    private MockRestServiceServer mockServer;
    @Autowired
    private DealClient calculatorClient;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSuccessRequestStatement() throws JsonProcessingException {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();

        List<LoanOfferDto> expectedResponse = List.of(new LoanOfferDto());

        // настройка
        mockServer.expect(requestTo("base_url/statement"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedResponse),
                        MediaType.APPLICATION_JSON));

        List<LoanOfferDto> actualResponse = calculatorClient.requestStatement(requestDto);

        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.size());
        assertEquals(actualResponse, expectedResponse);
    }
    @Test
    void testRequestOffers_4xxError() {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();

        mockServer.expect(requestTo("base_url/statement"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        // Пытаемся выполнить запрос, ожидаем исключение
        assertThrows(DealErrorException.class, () -> {
            calculatorClient.requestStatement(requestDto);
        });
    }

    @Test
    void testRequestOffers_5xxError() {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();
        // инициализируем requestDto необходимыми значениями

        // Установим мокированный ответ с ошибкой 5xx
        mockServer.expect(requestTo("base_url/statement"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        // Пытаемся выполнить запрос, ожидаем исключение
        assertThrows(DealErrorException.class, () -> {
            calculatorClient.requestStatement(requestDto);
        });
    }
}
