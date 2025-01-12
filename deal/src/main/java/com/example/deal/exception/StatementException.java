package com.example.deal.exception;

import lombok.Getter;

@Getter
public class StatementException extends RuntimeException{
    public StatementException(String message) {
        super(message);
    }
}
