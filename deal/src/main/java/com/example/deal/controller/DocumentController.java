package com.example.deal.controller;

import com.example.deal.dto.SesCodeDto;
import com.example.deal.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.UUID;

@RestController
@RequestMapping("/deal/document")
public class DocumentController {
    private final DocumentService documentService;

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Operation(summary = "Отправить документы", description = "Запрос отправления доументов на почту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @PostMapping(value = "/{statementId}/send")
    public ResponseEntity<Void> sendDocuments(@PathVariable UUID statementId){
        logger.debug("request /{statementId}/send start");
        this.documentService.sendDocuments(statementId);
        logger.debug("request /{statementId}/send start");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Подписать документы", description = "Запрос отправления проверочного кода на почту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @PostMapping(value = "/{statementId}/sign")
    public ResponseEntity<Void> signDocuments(@PathVariable UUID statementId){
        this.documentService.signDocuments(statementId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Завершить подписание документов", description = "Завершение принятия заявки на кредит")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(type = "object", example = "{\"field\":\"error\"}"))),
    })
    @PostMapping(value = "/{statementId}/code")
    public ResponseEntity<Void> verifyCode(@PathVariable UUID statementId, @RequestBody @Valid SesCodeDto dto){
        String sesCode = dto.getSesCode();
        this.documentService.verifyCode(statementId, sesCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
