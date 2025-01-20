package com.example.deal.service;

import com.example.deal.entity.Statement;
import com.example.deal.type.ApplicationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdminService {
    private final StatementService statementService;

    @Autowired
    public AdminService(StatementService statementService) {
        this.statementService = statementService;
    }

    public void changeStatus(UUID statementId){
        Statement statement = statementService.getStatement(statementId);
        statementService.checkStatus(statement, ApplicationStatus.PREPARE_DOCUMENTS);
        statementService.changeStatus(statement, ApplicationStatus.DOCUMENT_CREATED);
        statementService.saveStatement(statement);
    }


}
