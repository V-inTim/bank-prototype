package com.example.statement.handler;

import com.example.statement.exception.DealErrorException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class StatementHandler {

    private static final Logger logger = LoggerFactory.getLogger(StatementHandler.class);

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFormatException(InvalidFormatException ex) {
        logger.debug("Исключение InvalidFormatException");
        Map<String, String> errors = new HashMap<>();
        String fieldName = ex.getPath().get(0).getFieldName();
        String errorMessage = String.format("Invalid format for field: %s", ex.getValue());
        errors.put(fieldName, errorMessage);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleNotValidException(MethodArgumentNotValidException ex, WebRequest request){
        logger.debug("Исключение MethodArgumentNotValidException");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DealErrorException.class)
    public ResponseEntity<Map<String, Object>> handleCalculatorErrorException(DealErrorException ex, WebRequest request){
        logger.debug("Исключение DealErrorException");
        return new ResponseEntity<>(ex.getResponseBody(), ex.getStatusCode());
    }
}
