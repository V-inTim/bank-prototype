package com.example.deal.client;

import com.example.deal.dto.LoanOfferDto;
import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.exception.CalculatorErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CalculatorClient {
    private final RestClient restClient;
    @Autowired
    ObjectMapper objectMapper;



    @Autowired
    public CalculatorClient(@Value("${calculator.url}") String baseUrl, RestClient.Builder restClientBuilder){

        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public List<LoanOfferDto> requestOffers(LoanStatementRequestDto dto){

        ParameterizedTypeReference<List<LoanOfferDto>> responseType =
                new ParameterizedTypeReference<List<LoanOfferDto>>() {};

        List<LoanOfferDto> offers = restClient.post()
                .uri("/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    InputStream bodyStream = response.getBody();
                    Map<String, Object> responseBody = new HashMap<>();
                    if (bodyStream.available() != 0)
                        responseBody = objectMapper.readValue(bodyStream, Map.class);
                    HttpStatusCode statusCode = response.getStatusCode();
                    throw new CalculatorErrorException(statusCode, responseBody);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    InputStream bodyStream = response.getBody();
                    Map<String, Object> responseBody = new HashMap<>();
                    if (bodyStream.available() != 0)
                        responseBody = objectMapper.readValue(bodyStream, Map.class);
                    HttpStatusCode statusCode = response.getStatusCode();
                    throw new CalculatorErrorException(statusCode, responseBody);
                })
                .body(responseType);
        return offers;
    }

}
