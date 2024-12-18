package com.example.deal.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

@Getter
public class CalculatorErrorException extends RuntimeException{
    final private HttpStatusCode statusCode;
    final private Map<String, Object> responseBody;
    public CalculatorErrorException(HttpStatusCode statusCode, Map<String, Object> responseBody) {
        super("HTTP Error: " + statusCode.toString());
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

}


