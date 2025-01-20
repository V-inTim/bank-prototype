package com.example.gateway.client;

import com.example.gateway.dto.FinishRegistrationRequestDto;
import com.example.gateway.dto.SesCodeDto;
import com.example.gateway.exception.ClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Map;
import java.util.UUID;

@Component
public class DealClient {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;


    @Autowired
    public DealClient(@Value("${deal.url}") String baseUrl,
                            RestClient.Builder restClientBuilder, ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public void requestCalculateCredit(FinishRegistrationRequestDto dto, UUID statementId){
        try {
            restClient.post()
                .uri("/calculate/" + statementId.toString())
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
                        responseBody = Map.of("source", "Microservice Deal");
                    throw new ClientException(statusCode, responseBody);
                });
        } catch (RestClientException e) {
            Map<String, Object> responseBody = Map.of("source", "Microservice Deal");
            throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody);
        }
    }

    public void requestDocumentsSend(UUID statementId){
        try {
            restClient.post()
                .uri(String.format("/document/%s/send", statementId.toString()))
                .contentType(MediaType.APPLICATION_JSON)
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
                        responseBody = Map.of("source", "Microservice Deal");
                    throw new ClientException(statusCode, responseBody);
                });
        } catch (RestClientException e) {
            Map<String, Object> responseBody = Map.of("source", "Microservice Deal");
            throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody);
        }
    }

    public void requestDocumentsSign(UUID statementId){

        try {
            restClient.post()
                    .uri(String.format("/document/%s/sign", statementId.toString()))
                    .contentType(MediaType.APPLICATION_JSON)
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
                            responseBody = Map.of("source", "Microservice Deal");
                        throw new ClientException(statusCode, responseBody);
                    });
        } catch (RestClientException e) {
            Map<String, Object> responseBody = Map.of("source", "Microservice Deal");
            throw new ClientException(HttpStatus.INTERNAL_SERVER_ERROR, responseBody);
        }
    }

    public void requestDocumentsCode(UUID statementId, SesCodeDto dto) {

        try {
            restClient.post()
                    .uri(String.format("/document/%s/code", statementId.toString()))
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
                            responseBody = Map.of("source", "Microservice Deal");
                        throw new ClientException(statusCode, responseBody);
                    });
        } catch (RestClientException e) {
            Map<String, Object> responseBody = Map.of("source", "Microservice Deal");
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
