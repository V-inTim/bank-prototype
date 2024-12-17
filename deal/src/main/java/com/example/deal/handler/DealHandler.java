package com.example.deal.handler;

import com.example.deal.exception.CalculatorErrorException;
import com.example.deal.exception.DbException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
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
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFormatException(InvalidFormatException ex) {
        Map<String, String> errors = new HashMap<>();
        String fieldName = ex.getPath().get(0).getFieldName();
        String errorMessage = String.format("Invalid format for field: %s", ex.getValue());
        errors.put(fieldName, errorMessage);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleNotValidException(MethodArgumentNotValidException ex, WebRequest request){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CalculatorErrorException.class)
    public ResponseEntity<Map<String, Object>> handleCalculatorErrorException(CalculatorErrorException ex, WebRequest request){
        return new ResponseEntity<>(ex.getResponseBody(), ex.getStatusCode());
    }
    @ExceptionHandler(DbException.class)
    public ResponseEntity<Map<String, String>> handleDbException(DbException ex, WebRequest request){
        return new ResponseEntity<>(Map.of("message", ex.getMessage()), ex.getStatusCode());
    }
}
