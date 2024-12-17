package com.example.deal.dto;

import com.example.deal.type.EmploymentPosition;
import com.example.deal.type.EmploymentStatus;

import java.math.BigDecimal;

public class EmploymentDto {
    EmploymentStatus employmentStatus;
    String employerINN;
    BigDecimal salary;
    EmploymentPosition position;
    Integer workExperienceTotal;
    Integer workExperienceCurrent;
}
