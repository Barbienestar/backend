package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.PeriodReportsByHospitalResponse;
import com.itesm.application.dto.PeriodReportsByHospitalWithStockResponse;
import com.itesm.domain.repository.MedicinesHospitalsRepository;
import com.itesm.domain.repository.ReportsSnapshotRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetPeriodReportsByHospitalWithStockUseCaseTest {

    private ReportsSnapshotRepository reportsSnapshotRepository;
    private MedicinesHospitalsRepository medicinesHospitalsRepository;
    private GetPeriodReportsByHospitalWithStockUseCase useCase;

    @BeforeEach
    void setup() {
        reportsSnapshotRepository = mock(ReportsSnapshotRepository.class);
        medicinesHospitalsRepository = mock(MedicinesHospitalsRepository.class);
        useCase =
                new GetPeriodReportsByHospitalWithStockUseCase(reportsSnapshotRepository, medicinesHospitalsRepository);
    }

    @Test
    public void execute_shouldMergeReportsAndStock() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);

        PeriodReportsByHospitalResponse r1 = new PeriodReportsByHospitalResponse(LocalDate.of(2024, 1, 5), 10);
        PeriodReportsByHospitalResponse r2 = new PeriodReportsByHospitalResponse(LocalDate.of(2024, 1, 12), 7);

        when(reportsSnapshotRepository.getPeriodReports(1, start, end)).thenReturn(List.of(r1, r2));

        Object[] s1 = new Object[] {LocalDate.of(2024, 1, 5), 100};
        Object[] s2 = new Object[] {LocalDate.of(2024, 1, 12), 90};
        when(medicinesHospitalsRepository.getPeriodStock(1, start, end)).thenReturn(List.of(s1, s2));

        List<PeriodReportsByHospitalWithStockResponse> result = useCase.execute(1, start, end);

        assertEquals(2, result.size());

        assertEquals(LocalDate.of(2024, 1, 5), result.get(0).getDate());
        assertEquals(10, result.get(0).getTotalAcceptedReports());
        assertEquals(100, result.get(0).getTotalStock());

        assertEquals(LocalDate.of(2024, 1, 12), result.get(1).getDate());
        assertEquals(7, result.get(1).getTotalAcceptedReports());
        assertEquals(90, result.get(1).getTotalStock());
    }

    @Test
    public void execute_shouldUseZeroStockWhenNoStockData() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);

        PeriodReportsByHospitalResponse r1 = new PeriodReportsByHospitalResponse(LocalDate.of(2024, 1, 5), 10);

        when(reportsSnapshotRepository.getPeriodReports(1, start, end)).thenReturn(List.of(r1));
        when(medicinesHospitalsRepository.getPeriodStock(1, start, end)).thenReturn(List.of());

        List<PeriodReportsByHospitalWithStockResponse> result = useCase.execute(1, start, end);

        assertEquals(1, result.size());
        assertEquals(10, result.get(0).getTotalAcceptedReports());
        assertEquals(0, result.get(0).getTotalStock());
    }

    @Test
    public void execute_shouldReturnEmptyListWhenNoReports() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);

        when(reportsSnapshotRepository.getPeriodReports(1, start, end)).thenReturn(List.of());
        when(medicinesHospitalsRepository.getPeriodStock(1, start, end)).thenReturn(List.of());

        List<PeriodReportsByHospitalWithStockResponse> result = useCase.execute(1, start, end);

        assertEquals(0, result.size());
    }
}
