package com.example.deal.entity;

import com.example.deal.type.ApplicationStatus;
import com.example.deal.type.ChangeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusHistory {
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    private LocalDateTime time;
    @Enumerated(EnumType.STRING)
    private ChangeType changeType;
}
