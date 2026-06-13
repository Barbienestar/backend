package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.itesm.application.dto.MonthlyReportsDto;
import com.itesm.application.dto.MonthlyReportsResponse;
import com.itesm.domain.repository.MedicinesHospitalsRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetMonthlyReportsUseCaseTest {
    private MedicinesHospitalsRepository medicinesHospitalsRepository;
    private GetMonthlyReportsUseCase getMonthlyReportsUseCase;
    private MonthlyReportsDto defaultReq;

    @BeforeEach
    void setup() {
        medicinesHospitalsRepository = mock(MedicinesHospitalsRepository.class);
        getMonthlyReportsUseCase = new GetMonthlyReportsUseCase(medicinesHospitalsRepository);

        defaultReq = new MonthlyReportsDto(LocalDate.of(2026, 5, 1), LocalDate.of(2026, 6, 1));
    }

    @Test
    public void execute_ShouldReturnData() {
        MonthlyReportsResponse reports = new MonthlyReportsResponse();
        reports.setCurrentMonthReportCount(150);
        reports.setComparisonToLastMonth(new BigDecimal("0.5000"));

        when(medicinesHospitalsRepository.getMonthlyReports(1, defaultReq)).thenReturn(Optional.of(reports));

        Optional<MonthlyReportsResponse> result = getMonthlyReportsUseCase.execute(1, defaultReq);

        assertTrue(result.isPresent());
        assertEquals(150, result.get().getCurrentMonthReportCount());
        assertEquals(new BigDecimal("0.5000"), result.get().getComparisonToLastMonth());
    }

    @Test
    void shouldReturnZeroComparisonWhenBothMonthsAreEmpty() {
        MonthlyReportsResponse reports = new MonthlyReportsResponse();
        reports.setCurrentMonthReportCount(0);
        reports.setComparisonToLastMonth(new BigDecimal("0.00"));

        when(medicinesHospitalsRepository.getMonthlyReports(2, defaultReq)).thenReturn(Optional.of(reports));

        Optional<MonthlyReportsResponse> result = getMonthlyReportsUseCase.execute(2, defaultReq);

        assertTrue(result.isPresent());
        assertEquals(0, result.get().getCurrentMonthReportCount());
        assertEquals(new BigDecimal("0.00"), result.get().getComparisonToLastMonth());
    }

    @Test
    void shouldReturnFullGrowthWhenLastMonthWasZero() {
        MonthlyReportsResponse reports = new MonthlyReportsResponse();
        reports.setCurrentMonthReportCount(50);
        reports.setComparisonToLastMonth(new BigDecimal("1.00"));

        when(medicinesHospitalsRepository.getMonthlyReports(3, defaultReq)).thenReturn(Optional.of(reports));

        Optional<MonthlyReportsResponse> result = getMonthlyReportsUseCase.execute(3, defaultReq);

        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("1.00"), result.get().getComparisonToLastMonth());
    }

    @Test
    void shouldReturnNegativeGrowthWhenCurrentMonthIsZero() {
        MonthlyReportsResponse reports = new MonthlyReportsResponse();
        reports.setCurrentMonthReportCount(0);
        reports.setComparisonToLastMonth(new BigDecimal("-1.0000"));

        when(medicinesHospitalsRepository.getMonthlyReports(4, defaultReq)).thenReturn(Optional.of(reports));

        Optional<MonthlyReportsResponse> result = getMonthlyReportsUseCase.execute(4, defaultReq);

        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("-1.0000"), result.get().getComparisonToLastMonth());
    }

    @Test
    void shouldReturnEmptyOptionalWhenHospitalDoesNotExist() {
        when(medicinesHospitalsRepository.getMonthlyReports(99999, defaultReq)).thenReturn(Optional.empty());

        Optional<MonthlyReportsResponse> result = getMonthlyReportsUseCase.execute(99999, defaultReq);

        assertTrue(result.isEmpty());
    }
}
