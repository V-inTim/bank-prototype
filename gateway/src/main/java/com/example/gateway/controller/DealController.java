package com.example.gateway.controller;

import com.example.gateway.dto.FinishRegistrationRequestDto;
import com.example.gateway.dto.SesCodeDto;
import com.example.gateway.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/deal")
public class DealController {
    private final DealService dealService;
    private static final Logger logger = LoggerFactory.getLogger(DealController.class);

    @Autowired
    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @Operation(summary = "Посчитать кредит", description = "Посчитать offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @PostMapping("/calculate/{statementId}")
    public ResponseEntity<Void> calculateCredit(@PathVariable UUID statementId,
                                                @RequestBody FinishRegistrationRequestDto dto){
        logger.info("request /deal/calculate/{statementId} start");

        dealService.calculateCredit(statementId, dto);

        logger.info("request /deal/calculate/{statementId} finish");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Послать документы", description = "Послать запрос на отправку документов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @PostMapping("/document/{statementId}/send")
    public ResponseEntity<Void> sendDocuments(@PathVariable UUID statementId){
        logger.info("request /deal/document/{statementId}/send start");

        dealService.sendDocuments(statementId);

        logger.info("request /deal/document/{statementId}/send finish");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Запрос на отправку кода", description = "Запрос на отправку кода")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @PostMapping("/document/{statementId}/sign")
    public ResponseEntity<Void> signDocuments(@PathVariable UUID statementId){
        logger.info("request /deal/document/{statementId}/sign start");

        dealService.signDocuments(statementId);

        logger.info("request /deal/document/{statementId}/sign finish");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Отправить код", description = "Отправить код")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @PostMapping("/document/{statementId}/code")
    public ResponseEntity<Void> verifySESCode(@PathVariable UUID statementId, @RequestBody SesCodeDto dto){
        logger.info("request /deal/document/{statementId}/code start");

        dealService.documentCode(statementId, dto);

        logger.info("request /deal/document/{statementId}/code finish");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
