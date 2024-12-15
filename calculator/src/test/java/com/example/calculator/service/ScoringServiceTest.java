package com.example.calculator.service;

import com.example.calculator.dto.EmploymentDto;
import com.example.calculator.dto.ScoringDataDto;
import com.example.calculator.exception.ScoringException;
import com.example.calculator.type.EmploymentPosition;
import com.example.calculator.type.EmploymentStatus;
import com.example.calculator.type.Gender;
import com.example.calculator.type.MaritalStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ScoringServiceTest {
    @InjectMocks
    private ScoringService scoringService;

    @Mock
    private ScoringDataDto scoringDataMock;

    @Mock
    private EmploymentDto employmentMock;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        scoringService.standartRate = "0.05";

        // Настраиваем mock-объект ScoringDataDto
        lenient().when(scoringDataMock.getBirthdate()).thenReturn(LocalDate.of(1985, 1, 1));
        lenient().when(scoringDataMock.getIsInsuranceEnabled()).thenReturn(true);
        lenient().when(scoringDataMock.getIsSalaryClient()).thenReturn(true);
        lenient().when(scoringDataMock.getEmployment()).thenReturn(employmentMock);
        lenient().when(scoringDataMock.getAmount()).thenReturn(new BigDecimal("30000"));
        lenient().when(scoringDataMock.getGender()).thenReturn(Gender.FEMALE);
        lenient().when(scoringDataMock.getMaritalStatus()).thenReturn(MaritalStatus.MARRIED);

        // Настраиваем mock-объект EmploymentDto
        lenient().when(employmentMock.getWorkExperienceTotal()).thenReturn(20);
        lenient().when(employmentMock.getWorkExperienceCurrent()).thenReturn(5);
        lenient().when(employmentMock.getSalary()).thenReturn(new BigDecimal("2000"));
        lenient().when(employmentMock.getEmploymentStatus()).thenReturn(EmploymentStatus.BUSINESS_OWNER);
        lenient().when(employmentMock.getPosition()).thenReturn(EmploymentPosition.TOP_MANAGER);
    }
    @Test
    public void testPreEvaluateBothFalse(){
        BigDecimal expectedRate = new BigDecimal("0.12");
        BigDecimal actualRate = scoringService.preEvaluate(false, false);
        assertEquals(expectedRate, actualRate);
    }
    @Test
    public void testPreEvaluateTrueFalse(){
        BigDecimal expectedRate = new BigDecimal("0.10");
        BigDecimal actualRate = scoringService.preEvaluate(true, false);
        assertEquals(expectedRate, actualRate);
    }

    @Test
    public void testEvaluate() throws ScoringException {
        BigDecimal rate = scoringService.evaluate(scoringDataMock);
        assertEquals(new BigDecimal("0.02"), rate);
    }

    @Test
    public void testEvaluateThrowsExceptionForShortWorkExperienceTotal() {
        when(employmentMock.getWorkExperienceTotal()).thenReturn(17);
        ScoringException exception = assertThrows(ScoringException.class, () -> {
            scoringService.evaluate(scoringDataMock); });
        assertEquals("If WorkExperienceTotal is less than 18 months, the loan will not be issued.",
                exception.getMessage());
    }

    @Test
    public void testEvaluateThrowsExceptionForShortWorkExperienceCurrent() {
        when(employmentMock.getWorkExperienceCurrent()).thenReturn(2);
        ScoringException exception = assertThrows(ScoringException.class, () -> {
            scoringService.evaluate(scoringDataMock); });
        assertEquals("If WorkExperienceCurrent is less than 3 months, the loan will not be issued.",
                exception.getMessage());
    }

    @Test
    public void testEvaluateThrowsExceptionForHighLoanAmount() {
        when(employmentMock.getSalary()).thenReturn(new BigDecimal("1000"));
        when(scoringDataMock.getAmount()).thenReturn(new BigDecimal("50000"));
        ScoringException exception = assertThrows(ScoringException.class, () -> {
            scoringService.evaluate(scoringDataMock); });
        assertEquals("If amount is more than 24 salaries, the loan will not be issued.",
                exception.getMessage());
    }

    @Test
    public void testCheckAdultThrowsExceptionForTooYoung() {
        when(scoringDataMock.getBirthdate()).thenReturn(LocalDate.of(2010, 1, 1));
        ScoringException exception = assertThrows(ScoringException.class, () -> {
            scoringService.evaluate(scoringDataMock);
        });
        assertEquals("If age is more than 65 or less than 20, the loan will not be issued.",
                exception.getMessage());
    }

    @Test
    public void testCheckAdultDoesNotThrowExceptionForValidAge() {
        when(scoringDataMock.getBirthdate()).thenReturn(LocalDate.of(1980, 1, 1));
        assertDoesNotThrow(() -> { scoringService.evaluate(scoringDataMock); });
    }

    @Test
    public void testEvaluateEmploymentEmployedStatus() throws ScoringException {
        when(scoringDataMock.getBirthdate()).thenReturn(LocalDate.of(2002, 1, 1));
        when(scoringDataMock.getMaritalStatus()).thenReturn(MaritalStatus.SINGLE);

        when(employmentMock.getEmploymentStatus()).thenReturn(EmploymentStatus.SELF_EMPLOYED);
        BigDecimal rate = scoringService.evaluate(scoringDataMock);
        assertEquals(new BigDecimal("0.04"), rate);

        when(employmentMock.getEmploymentStatus()).thenReturn(EmploymentStatus.BUSINESS_OWNER);
        rate = scoringService.evaluate(scoringDataMock);
        assertEquals(new BigDecimal("0.03"), rate);

        when(employmentMock.getEmploymentStatus()).thenReturn(EmploymentStatus.EMPLOYED);
        rate = scoringService.evaluate(scoringDataMock);
        assertEquals(new BigDecimal("0.02"), rate);
    }

    @Test
    public void testEvaluateEmploymentPosition() throws ScoringException {
        when(scoringDataMock.getBirthdate()).thenReturn(LocalDate.of(2002, 1, 1));
        when(scoringDataMock.getMaritalStatus()).thenReturn(MaritalStatus.SINGLE);

        when(employmentMock.getPosition()).thenReturn(EmploymentPosition.TOP_MANAGER);
        BigDecimal rate = scoringService.evaluate(scoringDataMock);
        assertEquals(new BigDecimal("0.03"), rate);

    }


    @Test
    public void testEvaluateEmploymentWithUnemployedStatus() {
        when(employmentMock.getEmploymentStatus()).thenReturn(EmploymentStatus.UNEMPLOYED);
        ScoringException exception = assertThrows(ScoringException.class, () -> {
            scoringService.evaluate(scoringDataMock); });
        assertEquals("If unemployed, the loan will not be issued.", exception.getMessage());
    }

    @Test
    public void testEvaluateGender() throws ScoringException {
        when(scoringDataMock.getGender()).thenReturn(Gender.NON_BINARY);
        BigDecimal rate = scoringService.evaluate(scoringDataMock);
        assertEquals(new BigDecimal("0.07"), rate);

        when(scoringDataMock.getGender()).thenReturn(Gender.MALE);
        rate = scoringService.evaluate(scoringDataMock);
        assertEquals(new BigDecimal("0.02"), rate);

        when(scoringDataMock.getGender()).thenReturn(Gender.FEMALE);
        rate = scoringService.evaluate(scoringDataMock);
        assertEquals(new BigDecimal("0.02"), rate);
    }
    @Test
    public void testEvaluateMaritalStatus() throws ScoringException {
        when(scoringDataMock.getMaritalStatus()).thenReturn(MaritalStatus.SINGLE);
        when(scoringDataMock.getBirthdate()).thenReturn(LocalDate.of(2002, 1, 1));
        BigDecimal rate = scoringService.evaluate(scoringDataMock);
        assertEquals(new BigDecimal("0.03"), rate);

        when(scoringDataMock.getMaritalStatus()).thenReturn(MaritalStatus.MARRIED);
        rate = scoringService.evaluate(scoringDataMock);
        assertEquals(new BigDecimal("0.02"), rate);

        when(scoringDataMock.getMaritalStatus()).thenReturn(MaritalStatus.DIVORCED);
        rate = scoringService.evaluate(scoringDataMock);
        assertEquals(new BigDecimal("0.04"), rate);

        when(scoringDataMock.getMaritalStatus()).thenReturn(MaritalStatus.WIDOWED_WIDOWED);
        rate = scoringService.evaluate(scoringDataMock);
        assertEquals(new BigDecimal("0.03"), rate);
    }
}
