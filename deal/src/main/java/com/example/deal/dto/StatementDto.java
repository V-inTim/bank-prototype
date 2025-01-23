package com.example.deal.dto;

import com.example.deal.entity.AppliedOffer;
import com.example.deal.entity.StatusHistory;
import com.example.deal.type.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Клиент")
    private ClientDto client;

    @Schema(description = "Кредит")
    private CreditDto credit;

    @Schema(description = "Статус заявки")
    private ApplicationStatus status;

    @Schema(description = "Дата создания")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime creationDate;

    @Schema(description = "Предложение")
    private AppliedOffer appliedOffer;

    @Schema(description = "Дата подписания")
    private LocalDateTime signDate;

    @Schema(description = "Код подтверждения")
    private String sesCode;

    @Schema(description = "История статусов")
    private List<StatusHistory> statusHistory;
}
