package com.example.calculator.controller;

import com.example.calculator.dto.LoanStatementRequestDto;
import com.example.calculator.dto.ScoringDataDto;
import com.example.calculator.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    final OfferService offerService;

    @Autowired
    public CalculatorController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping(value = "/offers")
    public ResponseEntity<?> makeOffers(@Valid @RequestBody LoanStatementRequestDto requestData) {
        return new ResponseEntity<>(offerService.generateOffers(requestData), HttpStatus.OK);
    }

    @PostMapping(value = "/calc")
    public ResponseEntity<?> calculateCredit(@Valid @RequestBody ScoringDataDto requestData) {
        return new ResponseEntity<>(offerService.calculateCredit(requestData), HttpStatus.OK);
    }

}