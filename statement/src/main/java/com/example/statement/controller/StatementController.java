package com.example.statement.controller;

import com.example.statement.dto.LoanOfferDto;
import com.example.statement.dto.LoanStatementRequestDto;
import com.example.statement.service.StatementService;
import jakarta.validation.Valid;
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
    private final StatementService service;

    @Autowired
    public StatementController(StatementService service){
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<List<LoanOfferDto>> createStatement(@Valid @RequestBody LoanStatementRequestDto requestDto){
        List<LoanOfferDto> offers = service.createStatement(requestDto);
        return new ResponseEntity<List<LoanOfferDto>>(offers, HttpStatus.CREATED);
    }

    @PostMapping("/offer")
    public ResponseEntity<Void> applyOffer(@Valid @RequestBody LoanOfferDto requestDto){
        service.applyOffer(requestDto);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
