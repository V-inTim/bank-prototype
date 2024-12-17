package com.example.deal.controller;

import com.example.deal.dto.FinishRegistrationRequestDto;
import com.example.deal.dto.LoanOfferDto;
import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.service.DealService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/deal")
public class DealController {
    final DealService dealService;

    @Autowired
    public DealController(DealService dealService) {
        this.dealService = dealService;
    }


    @PostMapping(value = "/statement")
    public ResponseEntity<?> createStatement(@Valid @RequestBody LoanStatementRequestDto requestData) {
        List<LoanOfferDto> offers = dealService.createStatement(requestData);
        return new ResponseEntity<>(offers, HttpStatus.CREATED);
    }

//    @PostMapping(value = "/offer/select")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void applyOffer(@Valid @RequestBody LoanOfferDto requestData) {
//
//    }
//
//    @PostMapping(value = "/calculate/{statementId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void calculateCredit(@Valid @RequestBody FinishRegistrationRequestDto requestData) {
//
//    }

}
