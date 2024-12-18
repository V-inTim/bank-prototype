package com.example.deal.client;

import com.example.deal.dto.CreditDto;
import com.example.deal.dto.LoanOfferDto;
import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.dto.ScoringDataDto;
import com.example.deal.exception.CalculatorErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
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
        try {
            return restClient.post()
                    .uri("/offers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        HttpStatusCode statusCode = response.getStatusCode();
                        Map<String, Object> responseBody = convertBody(response);
                        throw new CalculatorErrorException(statusCode, responseBody);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        HttpStatusCode statusCode = response.getStatusCode();
                        Map<String, Object> responseBody = Map.of("source", "Microservice Calculator");
                        throw new CalculatorErrorException(statusCode, responseBody);
                    })
                    .body(responseType);
        } catch (RestClientException e) {
            Map<String, Object> responseBody = Map.of("source", "Microservice Calculator");
            throw new CalculatorErrorException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody);
        }
    }

    public CreditDto requestCalc(ScoringDataDto dto){

        try {
            return restClient.post()
                    .uri("/calc")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        HttpStatusCode statusCode = response.getStatusCode();
                        Map<String, Object> responseBody = convertBody(response);
                        throw new CalculatorErrorException(statusCode, responseBody);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        HttpStatusCode statusCode = response.getStatusCode();
                        Map<String, Object> responseBody = Map.of("source", "Microservice Calculator");
                        throw new CalculatorErrorException(statusCode, responseBody);
                    })
                    .body(CreditDto.class);
        } catch (RestClientException e) {
            Map<String, Object> responseBody = Map.of("source", "Microservice Calculator");
            throw new CalculatorErrorException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody);
        }
    }

    private Map<String, Object> convertBody(ClientHttpResponse response) throws IOException {
        InputStream bodyStream = response.getBody();
        Map<String, Object> responseBody = new HashMap<>();
        if (bodyStream.available() != 0)
            responseBody = objectMapper.readValue(bodyStream, Map.class);
        return responseBody;
    }

}
