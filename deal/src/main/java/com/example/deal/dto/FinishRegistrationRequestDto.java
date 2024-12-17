package com.example.deal.dto;

import com.example.deal.type.Gender;
import com.example.deal.type.MaritalStatus;

import java.time.LocalDate;

public class FinishRegistrationRequestDto {
    Gender gender;
    MaritalStatus maritalStatus;
    Integer dependentAmount;
    LocalDate passportIssueDate;
    String passportIssueBranch;
    EmploymentDto employment;
    String accountNumber;
}
