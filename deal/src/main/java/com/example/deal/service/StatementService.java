package com.example.deal.service;

import com.example.deal.entity.Statement;
import com.example.deal.entity.StatusHistory;
import com.example.deal.exception.StatementException;
import com.example.deal.repository.StatementRepository;
import com.example.deal.type.ApplicationStatus;
import com.example.deal.type.ChangeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StatementService {
    private final StatementRepository statementRepository;


    @Autowired
    public StatementService(StatementRepository statementRepository) {
        this.statementRepository = statementRepository;
    }

    public Statement getStatement(UUID statementId){
        Optional<Statement> optionalStatement = statementRepository.findById(statementId);
        if (optionalStatement.isEmpty()){
            throw new StatementException("Ресурс с данным id не сущетсвует.");
        }
        return optionalStatement.get();
    }

    public List<Statement> getAllStatements(){
        return statementRepository.findAll();
    }

    public void changeStatus(Statement statement, ApplicationStatus status){
        statement.setStatus(status);

        List<StatusHistory> history = statement.getStatusHistory();
        history.add(StatusHistory.builder()
                .status(status)
                .changeType(ChangeType.AUTOMATIC)
                .time(LocalDateTime.now()).build());
        statement.setStatusHistory(history);
    }

    public void checkStatus(Statement statement, ApplicationStatus expectedStatus){
        ApplicationStatus status = statement.getStatus();
        if (status != expectedStatus){
            throw new StatementException("В заявке неподходящий статус.");
        }
    }

    public void saveStatement(Statement statement) {
        statementRepository.save(statement);
    }
}
