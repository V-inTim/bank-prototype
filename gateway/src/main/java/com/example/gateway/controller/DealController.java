package com.example.gateway.controller;

import com.example.gateway.dto.FinishRegistrationRequestDto;
import com.example.gateway.dto.SesCodeDto;
import com.example.gateway.service.DealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/deal")
public class DealController {
    private final DealService dealService;


    @Autowired
    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @PostMapping("/calculate/{statementId}")
    public ResponseEntity<Void> calculateCredit(@PathVariable UUID statementId,
                                                @RequestBody FinishRegistrationRequestDto dto){
        dealService.calculateCredit(statementId, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/document/{statementId}/send")
    public ResponseEntity<Void> sendDocuments(@PathVariable UUID statementId){
        dealService.sendDocuments(statementId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/document/{statementId}/sign")
    public ResponseEntity<Void> signDocuments(@PathVariable UUID statementId){
        dealService.signDocuments(statementId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/document/{statementId}/code")
    public ResponseEntity<Void> verifySESCode(@PathVariable UUID statementId, @RequestBody SesCodeDto dto){
        dealService.documentCode(statementId, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
