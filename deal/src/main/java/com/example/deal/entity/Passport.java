package com.example.deal.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Passport {
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID passport;
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}
