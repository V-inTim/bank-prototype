package com.example.gateway.controller;

import com.example.gateway.dto.LoanOfferDto;
import com.example.gateway.dto.LoanStatementRequestDto;
import com.example.gateway.service.StatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final StatementService statementService;
    private static final Logger logger = LoggerFactory.getLogger(StatementController.class);

    @Autowired
    public StatementController(StatementService statementService) {
        this.statementService = statementService;
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
    @PostMapping("")
    public ResponseEntity<List<LoanOfferDto>> createStatement(@RequestBody LoanStatementRequestDto dto){
        logger.info("request /statement start");

        List<LoanOfferDto> offers = statementService.createStatement(dto);

        logger.info("request /statement finish");
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
    @PostMapping("/offer")
    public ResponseEntity<Void> applyOffer(@RequestBody LoanOfferDto dto){
        logger.info("request /statement/offer start");

        statementService.applyOffer(dto);

        logger.info("request /statement/offer finish");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
