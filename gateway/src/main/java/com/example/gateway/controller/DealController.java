package com.example.gateway.controller;

import com.example.gateway.client.DealClient;
import com.example.gateway.dto.FinishRegistrationRequestDto;
import com.example.gateway.dto.SesCodeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/deal")
public class DealController {
    private final DealClient dealClient;

    @Autowired
    public DealController(DealClient dealClient) {
        this.dealClient = dealClient;
    }

    @PostMapping("/calculate/{statementId}")
    public ResponseEntity<Void> calculateCredit(@PathVariable UUID statementId,
                                                @RequestBody FinishRegistrationRequestDto dto){
        dealClient.requestCalculateCredit(dto, statementId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/document/{statementId}/send")
    public ResponseEntity<Void> sendDocuments(@PathVariable UUID statementId){
        dealClient.requestDocumentsSend(statementId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/document/{statementId}/sign")
    public ResponseEntity<Void> signDocuments(@PathVariable UUID statementId){
        dealClient.requestDocumentsSign(statementId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/document/{statementId}/code")
    public ResponseEntity<Void> verifySESCode(@PathVariable UUID statementId, @RequestBody SesCodeDto dto){
        dealClient.requestDocumentsCode(statementId, dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
