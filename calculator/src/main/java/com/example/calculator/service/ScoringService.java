package com.example.calculator.service;

import com.example.calculator.dto.EmploymentDto;
import com.example.calculator.dto.ScoringDataDto;
import com.example.calculator.exception.ScoringException;
import com.example.calculator.type.Gender;
import com.example.calculator.type.MaritalStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
public class ScoringService {
    @Value("${app.rate}")
    String standartRate;

    public BigDecimal preEvaluate(boolean isInsuranceEnabled,
                                  boolean isSalaryClient){
        BigDecimal rate = new BigDecimal(standartRate);
        if (!isInsuranceEnabled && !isSalaryClient){
            rate = rate.add(new BigDecimal("0.07"));
        } else if (!isInsuranceEnabled){
            rate = rate.add(new BigDecimal("0.02"));
        }else if (!isSalaryClient){
            rate = rate.add(new BigDecimal("0.05"));
        }
        if (rate.compareTo(new BigDecimal("0")) <= 0){
            return new BigDecimal("0.02");
        }
        return rate;
    }

    public BigDecimal evaluate(ScoringDataDto scoringData) throws ScoringException {
        checkAdult(scoringData.getBirthdate());
        BigDecimal rate = preEvaluate(scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());
        rate = evaluateEmployment(rate, scoringData.getEmployment(),scoringData.getAmount());
        rate = evaluateGender(rate, scoringData.getGender(), scoringData.getBirthdate());
        rate = evaluateMaritalStatus(rate, scoringData.getMaritalStatus());
        if (rate.compareTo(new BigDecimal("0.02")) <= 0)
            return new BigDecimal("0.02");
        return rate;
    }

    private BigDecimal evaluateEmployment(
            BigDecimal rate, EmploymentDto employment, BigDecimal amount) throws ScoringException {

        BigDecimal value = BigDecimal.valueOf(employment.getWorkExperienceTotal());
        if (value.compareTo(new BigDecimal(18)) < 0)
            throw new ScoringException("If WorkExperienceTotal is less than 18 months, the loan will not be issued.");

        value = BigDecimal.valueOf(employment.getWorkExperienceCurrent());
        if (value.compareTo(new BigDecimal(3)) < 0)
            throw new ScoringException("If WorkExperienceCurrent is less than 3 months, the loan will not be issued.");

        value = employment.getSalary().multiply(new BigDecimal(24));
        if (amount.compareTo(value) > 0)
            throw new ScoringException("If amount is more than 24 salaries, the loan will not be issued.");

        switch (employment.getEmploymentStatus()){
            case UNEMPLOYED:
                throw new ScoringException("If unemployed, the loan will not be issued.");
            case SELF_EMPLOYED:
                rate = rate.add(new BigDecimal("0.02"));
                break;
            case BUSINESS_OWNER:
                rate = rate.add(new BigDecimal("0.01"));
                break;
        }
        switch (employment.getPosition()){
            case MID_MANAGER:
                rate = rate.subtract(new BigDecimal("0.02"));
                break;
            case TOP_MANAGER:
                rate = rate.subtract(new BigDecimal("0.03"));
                break;
        }
        return rate;
    }

    private BigDecimal evaluateGender(BigDecimal rate, Gender gender, LocalDate birthdate)  {
        int age = Period.between(birthdate, LocalDate.now()).getYears();
        switch (gender){
            case FEMALE:
                if (age > 32 && age < 60){
                    rate = rate.subtract(new BigDecimal("0.03"));
                }
                break;
            case MALE:
                if (age > 30 && age < 55){
                    rate = rate.subtract(new BigDecimal("0.03"));
                }
                break;
            case NON_BINARY:
                rate = rate.add(new BigDecimal("0.07"));
                break;
        }
        return rate;
    }

    private BigDecimal evaluateMaritalStatus(BigDecimal rate, MaritalStatus maritalStatus) {
        switch (maritalStatus){
            case MARRIED:
                rate = rate.subtract(new BigDecimal("0.03"));
                break;
            case DIVORCED:
                rate = rate.add(new BigDecimal("0.01"));
                break;
        }
        return rate;
    }

    private void checkAdult(LocalDate birthdate) throws ScoringException {
        Period age = Period.between(birthdate, LocalDate.now());
        if (age.getYears() > 65 || age.getYears() < 20)
            throw new ScoringException("If age is more than 65 or less than 20, the loan will not be issued.");
    }


}
