package com.example.deal.controller;

import com.example.deal.dto.StatementDto;
import com.example.deal.service.AdminService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal/admin")
public class AdminController {
    private final AdminService adminService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Изменить статус", description = "Изменить статус statement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @PutMapping("/statement/{statementId}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable UUID statementId){
        logger.info("request /statement/{statementId}/status start");

        adminService.changeStatus(statementId);

        logger.info("request /statement/{statementId}/status finish");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Запрос statement", description = "Запрос statement по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StatementDto.class))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @GetMapping("/statement/{statementId}")
    public ResponseEntity<StatementDto> getStatement(@PathVariable UUID statementId){
        logger.info("request /statement/{statementId} start");

        StatementDto dto = adminService.getStatement(statementId);

        logger.info("request /statement/{statementId} finish");
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Operation(summary = "Запрос на все statement", description = "Запрос на все statement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = StatementDto.class)))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @GetMapping("/statement")
    public ResponseEntity<List<StatementDto>> getAllStatements(){
        logger.info("request /statement start");

        List<StatementDto> dtoList = adminService.getAllStatements();

        logger.info("request /statement finish");
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}
