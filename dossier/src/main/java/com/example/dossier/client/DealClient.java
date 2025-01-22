package com.example.dossier.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Component
public class DealClient {
    private final RestClient restClient;

    private static final Logger logger = LoggerFactory.getLogger(DealClient.class);

    @Autowired
    public DealClient(@Value("${deal.admin.url}") String baseUrl,  RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public void requestChangeStatus(UUID statementId){
        try {
            restClient.put()
                    .uri(String.format("/statement/%s/status", statementId.toString()))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        logger.info("Request changeType, Server Error {}", response.getStatusCode());
                        System.out.println(response.getStatusCode());
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        logger.info("Request changeType, Client Error {}", response.getStatusCode());
                    })
                    .onStatus(HttpStatusCode::is2xxSuccessful, (request, response) -> {
                        logger.info("Request changeType, Ok");
                    })
                    .toEntity(Void.class);
        } catch (RestClientException e) {
            logger.info("Error of RestClient request");
        }
    }
}
