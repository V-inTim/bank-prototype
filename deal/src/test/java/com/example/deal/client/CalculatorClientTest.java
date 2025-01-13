package com.example.deal.client;

import com.example.deal.dto.LoanOfferDto;
import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.exception.CalculatorErrorException;
import com.example.deal.service.RefusalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@RestClientTest(CalculatorClient.class)
@TestPropertySource(properties = "calculator.url=base_url")
public class CalculatorClientTest {
    private final MockRestServiceServer mockServer;

    private final ObjectMapper objectMapper;
    private final CalculatorClient calculatorClient;

    @MockBean
    private RefusalService refusalService;

    @Autowired
    public CalculatorClientTest(MockRestServiceServer mockServer, ObjectMapper objectMapper, CalculatorClient calculatorClient) {
        this.mockServer = mockServer;
        this.objectMapper = objectMapper;
        this.calculatorClient = calculatorClient;
    }

    @Test
    void testRequestOffers_Success() throws JsonProcessingException {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();

        LoanOfferDto offer = new LoanOfferDto();
        List<LoanOfferDto> expectedResponse = List.of(offer);

        mockServer.expect(requestTo("base_url/offers"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withSuccess(objectMapper.writeValueAsString(expectedResponse), MediaType.APPLICATION_JSON));

        List<LoanOfferDto> actualResponse = calculatorClient.requestOffers(requestDto);

        assertNotNull(actualResponse);
        assertEquals(1, actualResponse.size());
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testRequestOffers_4xxError() {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();

        mockServer.expect(requestTo("base_url/offers"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        CalculatorErrorException exception = assertThrows(CalculatorErrorException.class, () -> {
            calculatorClient.requestOffers(requestDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertNotNull(exception.getResponseBody());
    }

    @Test
    void testRequestOffers_5xxError() {
        LoanStatementRequestDto requestDto = new LoanStatementRequestDto();

        mockServer.expect(requestTo("base_url/offers"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        CalculatorErrorException exception = assertThrows(CalculatorErrorException.class, () -> {
            calculatorClient.requestOffers(requestDto);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertNotNull(exception.getResponseBody());
    }
}
