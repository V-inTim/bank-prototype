package com.example.deal.dto;

import com.example.deal.entity.AppliedOffer;
import com.example.deal.entity.StatusHistory;
import com.example.deal.type.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatementDto {
    private ClientDto client;

    private CreditDto credit;

    private ApplicationStatus status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime creationDate;

    private AppliedOffer appliedOffer;

    private LocalDateTime signDate;

    private String sesCode;

    private List<StatusHistory> statusHistory;
}
