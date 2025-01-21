package com.example.deal.controller;

import com.example.deal.dto.StatementDto;
import com.example.deal.service.AdminService;
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

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/statement/{statementId}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable UUID statementId){
        adminService.changeStatus(statementId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/statement/{statementId}")
    public ResponseEntity<StatementDto> getStatement(@PathVariable UUID statementId){
        StatementDto dto = adminService.getStatement(statementId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @GetMapping("/statement")
    public ResponseEntity<List<StatementDto>> getAllStatements(){
        List<StatementDto> dtoList = adminService.getAllStatements();
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}
