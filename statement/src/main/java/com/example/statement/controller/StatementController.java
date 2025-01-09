package com.example.statement.controller;

import com.example.statement.dto.LoanOfferDto;
import com.example.statement.dto.LoanStatementRequestDto;
import com.example.statement.service.StatementService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statement")
public class StatementController {
    private final StatementService service;

    private static final Logger logger = LoggerFactory.getLogger(StatementController.class);

    @Autowired
    public StatementController(StatementService service){
        this.service = service;
    }

    @PostMapping("")
    @Operation(summary = "Создать statement", description = "Прескоринг + запрос на расчёт возможных условий кредита")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = LoanOfferDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    public ResponseEntity<List<LoanOfferDto>> createStatement(@Valid @RequestBody LoanStatementRequestDto requestDto){
        logger.info("Запрос на /statement");

        List<LoanOfferDto> offers = service.createStatement(requestDto);
        logger.debug("Сгенерированный List<LoanOfferDto>: {}", offers);
        logger.info("Ответ на /statement");
        return new ResponseEntity<List<LoanOfferDto>>(offers, HttpStatus.CREATED);
    }

    @PostMapping("/offer")
    @Operation(summary = "Выбор одного из предложений", description = "Выбор одного из предложений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    public ResponseEntity<Void> applyOffer(@Valid @RequestBody LoanOfferDto requestDto){
        logger.info("Запрос на /statement/offer");

        service.applyOffer(requestDto);

        logger.info("Ответ на /statement/offer");
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
