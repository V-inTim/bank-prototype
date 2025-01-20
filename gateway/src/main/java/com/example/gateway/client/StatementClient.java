package com.example.gateway.client;

import com.example.gateway.dto.LoanOfferDto;
import com.example.gateway.dto.LoanStatementRequestDto;
import com.example.gateway.exception.ClientException;
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
public class StatementClient {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;


    @Autowired
    public StatementClient(@Value("${statement.url}") String baseUrl,
                      RestClient.Builder restClientBuilder, ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public List<LoanOfferDto> requestCreateStatement(LoanStatementRequestDto dto){
        ParameterizedTypeReference<List<LoanOfferDto>> responseType =
                new ParameterizedTypeReference<>() {};
        try {
            return restClient.post()
                    .uri("")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        HttpStatusCode statusCode = response.getStatusCode();
                        Map<String, Object> responseBody = convertBody(response);
                        throw new ClientException(statusCode, responseBody);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        HttpStatusCode statusCode = response.getStatusCode();
                        Map<String, Object> responseBody = convertBody(response);

                        if (!responseBody.containsKey("source"))
                            responseBody = Map.of("source", "Microservice Statement");
                        throw new ClientException(statusCode, responseBody);
                    })
                    .body(responseType);
        } catch (RestClientException e) {
            Map<String, Object> responseBody = Map.of("source", "Microservice Statement");
            throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody);
        }
    }

    public void requestApplyOffer(LoanOfferDto dto){
        try {
            restClient.post()
                    .uri("/offer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(dto)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        HttpStatusCode statusCode = response.getStatusCode();
                        Map<String, Object> responseBody = convertBody(response);
                        throw new ClientException(statusCode, responseBody);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        HttpStatusCode statusCode = response.getStatusCode();
                        Map<String, Object> responseBody = convertBody(response);

                        if (!responseBody.containsKey("source"))
                            responseBody = Map.of("source", "Microservice Statement");
                        throw new ClientException(statusCode, responseBody);
                    });
        } catch (RestClientException e) {
            Map<String, Object> responseBody = Map.of("source", "Microservice Statement");
            throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody);
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
