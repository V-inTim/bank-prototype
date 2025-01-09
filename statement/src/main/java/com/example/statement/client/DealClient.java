package com.example.statement.client;

import com.example.statement.dto.LoanOfferDto;
import com.example.statement.dto.LoanStatementRequestDto;
import com.example.statement.exception.DealErrorException;
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
public class DealClient {
    private final RestClient restClient;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    public DealClient(@Value("${deal.url}") String baseUrl, RestClient.Builder restClientBuilder){

        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public List<LoanOfferDto> requestStatement(LoanStatementRequestDto dto){

        ParameterizedTypeReference<List<LoanOfferDto>> responseType =
                new ParameterizedTypeReference<>() {};
        try {
            return restClient.post()
                    .uri("/statement")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        HttpStatusCode statusCode = response.getStatusCode();
                        Map<String, Object> responseBody = convertBody(response);
                        throw new DealErrorException(statusCode, responseBody);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        HttpStatusCode statusCode = response.getStatusCode();
                        Map<String, Object> responseBody = Map.of("source", "Microservice Deal");
                        throw new DealErrorException(statusCode, responseBody);
                    })
                    .body(responseType);
        } catch (RestClientException e) {
            Map<String, Object> responseBody = Map.of("source", "Microservice Deal");
            throw new DealErrorException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody);
        }
    }

    public void requestStatementOffer(LoanOfferDto dto){
        try {
            restClient.post()
                .uri("/offer/select")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    HttpStatusCode statusCode = response.getStatusCode();
                    Map<String, Object> responseBody = convertBody(response);
                    throw new DealErrorException(statusCode, responseBody);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                    HttpStatusCode statusCode = response.getStatusCode();
                    Map<String, Object> responseBody = Map.of("source", "Microservice Deal");
                    throw new DealErrorException(statusCode, responseBody);
                });
        } catch (RestClientException e) {
            Map<String, Object> responseBody = Map.of("source", "Microservice Deal");
            throw new DealErrorException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody);
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
