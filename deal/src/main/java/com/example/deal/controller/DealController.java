package com.example.deal.controller;

import com.example.deal.dto.FinishRegistrationRequestDto;
import com.example.deal.dto.LoanOfferDto;
import com.example.deal.dto.LoanStatementRequestDto;
import com.example.deal.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/deal")
public class DealController {
    final DealService dealService;
    private static final Logger logger = LoggerFactory.getLogger(DealController.class);

    @Autowired
    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @Operation(summary = "Создать statement", description = "Создать, сохранить statement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = LoanOfferDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @PostMapping(value = "/statement")
    public ResponseEntity<List<LoanOfferDto>> createStatement(@Valid @RequestBody LoanStatementRequestDto requestData) {
        logger.info("Запрос на /statement: {}", requestData);

        List<LoanOfferDto> offers = dealService.createStatement(requestData);

        logger.info("Ответ на /calculator/offers: {}", offers);
        return new ResponseEntity<>(offers, HttpStatus.CREATED);
    }

    @Operation(summary = "Установить offer", description = "Сохранить выбранный offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @PostMapping(value = "/offer/select")
    public ResponseEntity<Void> applyOffer(@Valid @RequestBody LoanOfferDto requestData) {
        logger.info("Запрос на /offer/select: {}", requestData);

        dealService.applyOffer(requestData);

        logger.info("Ответ на /calculator/offers не предусмотрен");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Посчитать кредит", description = "Получить сумму кредита и сохранить")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @PostMapping(value = "/calculate/{statementId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> calculateCredit(
            @PathVariable UUID statementId,
            @Valid @RequestBody FinishRegistrationRequestDto requestData) {
        logger.info("Запрос на /calculate/{statementId}: {}", requestData);

        dealService.calculateCredit(requestData, statementId);

        logger.info("Ответ на /calculate/{statementId} не предусмотрен");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
