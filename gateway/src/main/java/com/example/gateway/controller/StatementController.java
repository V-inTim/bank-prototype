package com.example.gateway.controller;

import com.example.gateway.client.StatementClient;
import com.example.gateway.dto.LoanOfferDto;
import com.example.gateway.dto.LoanStatementRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/statement")
public class StatementController {
    private final StatementClient statementClient;

    @Autowired
    public StatementController(StatementClient statementClient) {
        this.statementClient = statementClient;
    }

    @PostMapping("")
    public ResponseEntity<List<LoanOfferDto>> createStatement(@RequestBody LoanStatementRequestDto dto){
        List<LoanOfferDto> offers = statementClient.requestCreateStatement(dto);
        return new ResponseEntity<>(offers, HttpStatus.CREATED);
    }

    @PostMapping("/offer")
    public ResponseEntity<Void> applyOffer(@RequestBody LoanOfferDto dto){
        statementClient.requestApplyOffer(dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
