package com.itesm.application.usecase;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.itesm.application.dto.MonthlyReportsResponse;
import com.itesm.domain.repository.MedicinesHospitalsRepository;

public class GetMonthlyReportsUseCaseTest {
    private MedicinesHospitalsRepository medicinesHospitalsRepository;
    private GetMonthlyReportsUseCase getMonthlyReportsUseCase;

    @BeforeEach
    void setup() {
        medicinesHospitalsRepository = mock(MedicinesHospitalsRepository.class);
        getMonthlyReportsUseCase = new GetMonthlyReportsUseCase(medicinesHospitalsRepository);
    }

    @Test
    public void execute_ShouldReturnData() {
        MonthlyReportsResponse reports = new MonthlyReportsResponse();
        reports.setCurrentMonthReportCount(150);
        reports.setComparisonToLastMonth(new BigDecimal("0.5000"));

        when(medicinesHospitalsRepository.getMonthlyReports(1)).thenReturn(Optional.of(reports));

        Optional<MonthlyReportsResponse> result = getMonthlyReportsUseCase.execute(1);

        assertTrue(result.isPresent());
        assertEquals(150, result.get().getCurrentMonthReportCount());
        assertEquals(new BigDecimal("0.5000"), result.get().getComparisonToLastMonth());
    }

    @Test
    void shouldReturnZeroComparisonWhenBothMonthsAreEmpty() {
        MonthlyReportsResponse reports = new MonthlyReportsResponse();
        reports.setCurrentMonthReportCount(0);
        reports.setComparisonToLastMonth(new BigDecimal("0.00"));

        when(medicinesHospitalsRepository.getMonthlyReports(2)).thenReturn(Optional.of(reports));

        Optional<MonthlyReportsResponse> result = getMonthlyReportsUseCase.execute(2);

        assertTrue(result.isPresent());
        assertEquals(0, result.get().getCurrentMonthReportCount());
        assertEquals(new BigDecimal("0.00"), result.get().getComparisonToLastMonth());
    }

    @Test
    void shouldReturnFullGrowthWhenLastMonthWasZero() {
        MonthlyReportsResponse reports = new MonthlyReportsResponse();
        reports.setCurrentMonthReportCount(50);
        reports.setComparisonToLastMonth(new BigDecimal("1.00"));

        when(medicinesHospitalsRepository.getMonthlyReports(3)).thenReturn(Optional.of(reports));

        Optional<MonthlyReportsResponse> result = getMonthlyReportsUseCase.execute(3);

        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("1.00"), result.get().getComparisonToLastMonth());
    }

    @Test
    void shouldReturnNegativeGrowthWhenCurrentMonthIsZero() {
        MonthlyReportsResponse reports = new MonthlyReportsResponse();
        reports.setCurrentMonthReportCount(0);
        reports.setComparisonToLastMonth(new BigDecimal("-1.0000"));

        when(medicinesHospitalsRepository.getMonthlyReports(4)).thenReturn(Optional.of(reports));

        Optional<MonthlyReportsResponse> result = getMonthlyReportsUseCase.execute(4);

        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("-1.0000"), result.get().getComparisonToLastMonth());
    }

    @Test
    void shouldReturnEmptyOptionalWhenHospitalDoesNotExist() {
        when(medicinesHospitalsRepository.getMonthlyReports(99999)).thenReturn(Optional.empty());

        Optional<MonthlyReportsResponse> result = getMonthlyReportsUseCase.execute(99999);

        assertTrue(result.isEmpty());
    }

}
