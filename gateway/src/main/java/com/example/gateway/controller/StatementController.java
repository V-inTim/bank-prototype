package com.example.gateway.controller;

import com.example.gateway.dto.LoanOfferDto;
import com.example.gateway.dto.LoanStatementRequestDto;
import com.example.gateway.service.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statement")
public class StatementController {
    private final StatementService statementService;

    @Autowired
    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }


    @PostMapping("")
    public ResponseEntity<List<LoanOfferDto>> createStatement(@RequestBody LoanStatementRequestDto dto){
        List<LoanOfferDto> offers = statementService.createStatement(dto);
        return new ResponseEntity<>(offers, HttpStatus.CREATED);
    }

    @PostMapping("/offer")
    public ResponseEntity<Void> applyOffer(@RequestBody LoanOfferDto dto){
        statementService.applyOffer(dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
