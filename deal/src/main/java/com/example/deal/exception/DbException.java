package com.example.deal.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class DbException extends RuntimeException{
    final private HttpStatusCode statusCode;
    public DbException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
