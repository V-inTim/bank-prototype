package com.example.deal.handler;

import com.example.deal.exception.CalculatorErrorException;
import com.example.deal.exception.DbException;
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
public class DealHandler{
    private static final Logger logger = LoggerFactory.getLogger(DealHandler.class);

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFormatException(InvalidFormatException ex) {
        Map<String, String> errors = new HashMap<>();
        String fieldName = ex.getPath().get(0).getFieldName();
        String errorMessage = String.format("Invalid format for field: %s", ex.getValue());
        errors.put(fieldName, errorMessage);
        logger.debug("DealHandler, InvalidFormatException");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleNotValidException(MethodArgumentNotValidException ex, WebRequest request){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        logger.debug("DealHandler, MethodArgumentNotValidException");
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CalculatorErrorException.class)
    public ResponseEntity<Map<String, Object>> handleCalculatorErrorException(CalculatorErrorException ex, WebRequest request){
        logger.debug("DealHandler, CalculatorErrorException");
        return new ResponseEntity<>(ex.getResponseBody(), ex.getStatusCode());
    }
    @ExceptionHandler(DbException.class)
    public ResponseEntity<Map<String, String>> handleDbException(DbException ex, WebRequest request){
        logger.debug("DealHandler, DbException");
        return new ResponseEntity<>(Map.of("message", ex.getMessage()), ex.getStatusCode());
    }
}
