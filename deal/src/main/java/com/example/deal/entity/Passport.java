package com.example.deal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Passport {
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}