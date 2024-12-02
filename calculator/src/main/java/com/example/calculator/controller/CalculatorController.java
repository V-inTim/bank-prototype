package com.example.calculator.controller;

import com.example.calculator.dto.CreditDto;
import com.example.calculator.dto.LoanOfferDto;
import com.example.calculator.dto.LoanStatementRequestDto;
import com.example.calculator.dto.ScoringDataDto;
import com.example.calculator.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "Calculator API")
@RestController
@RequestMapping("/calculator")
public class CalculatorController {
    final OfferService offerService;
    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    @Autowired
    public CalculatorController(OfferService offerService) {
        this.offerService = offerService;
    }

    @Operation(summary = "Посчитать offers", description = "Сформировать список из 4 LoanOfferDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = LoanOfferDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @PostMapping(value = "/offers")
    public ResponseEntity<?> makeOffers(@Valid @RequestBody LoanStatementRequestDto requestData) {
        logger.info("Запрос на /calculator/offers: {}", requestData);

        List<LoanOfferDto> response = offerService.generateOffers(requestData);

        logger.info("Ответ на /calculator/offers: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Посчитать credit", description = "Просчитать конечный вариант кредита CreditDto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = CreditDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @PostMapping(value = "/calc")
    public ResponseEntity<?> calculateCredit(@Valid @RequestBody ScoringDataDto requestData) {
        logger.info("Запрос на /calculator/calc: {}", requestData);

        CreditDto response = offerService.calculateCredit(requestData);

        logger.info("Ответ на /calculator/calc: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}