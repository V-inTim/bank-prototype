package com.example.statement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

@Getter
public class DealErrorException extends RuntimeException{
    private final HttpStatusCode statusCode;
    private final Map<String, Object> responseBody;
    public DealErrorException(HttpStatusCode statusCode, Map<String, Object> responseBody) {
        super("HTTP Error: " + statusCode.toString());
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

}


