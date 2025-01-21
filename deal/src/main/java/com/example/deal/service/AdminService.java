package com.example.deal.service;

import com.example.deal.dto.StatementDto;
import com.example.deal.entity.Statement;
import com.example.deal.mapper.StatementMapper;
import com.example.deal.type.ApplicationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminService {
    private final StatementService statementService;
    private final StatementMapper statementMapper;

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    public AdminService(StatementService statementService, StatementMapper statementMapper, StatementMapper statementMapper1) {
        this.statementService = statementService;
        this.statementMapper = statementMapper1;
    }

    public void changeStatus(UUID statementId){
        Statement statement = statementService.getStatement(statementId);
        statementService.checkStatus(statement, ApplicationStatus.PREPARE_DOCUMENTS);
        statementService.changeStatus(statement, ApplicationStatus.DOCUMENT_CREATED);
        statementService.saveStatement(statement);
        logger.info("changeStatus save statement");
    }


    public StatementDto getStatement(UUID statementId) {
        Statement statement = statementService.getStatement(statementId);
        logger.info("getStatement get statement");

        return statementMapper.statementToDto(statement);
    }

    public List<StatementDto> getAllStatements() {
        List<Statement> statements = statementService.getAllStatements();
        logger.info("getAllStatements get all statements");

        return statements.stream().map(statementMapper::statementToDto).toList();

    }
}
