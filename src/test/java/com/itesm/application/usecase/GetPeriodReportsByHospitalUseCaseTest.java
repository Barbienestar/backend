package com.itesm.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.itesm.application.dto.PeriodReportsByHospitalResponse;
import com.itesm.domain.repository.ReportsSnapshotRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GetPeriodReportsByHospitalUseCaseTest {

    private ReportsSnapshotRepository repository;
    private GetPeriodReportsByHospitalUseCase useCase;

    @BeforeEach
    void setup() {
        repository = mock(ReportsSnapshotRepository.class);
        useCase = new GetPeriodReportsByHospitalUseCase(repository);
    }

    @Test
    public void execute_shouldReturnReportList() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);
        PeriodReportsByHospitalResponse r1 = new PeriodReportsByHospitalResponse(
                LocalDate.of(2024, 1, 5), 10);
        PeriodReportsByHospitalResponse r2 = new PeriodReportsByHospitalResponse(
                LocalDate.of(2024, 1, 12), 7);

        when(repository.getPeriodReports(1, start, end))
                .thenReturn(List.of(r1, r2));

        List<PeriodReportsByHospitalResponse> result = useCase.execute(1, start, end);

        assertEquals(2, result.size());
        assertEquals(LocalDate.of(2024, 1, 5), result.get(0).getDate());
        assertEquals(10, result.get(0).getTotalAcceptedReturns());
    }

    @Test
    public void execute_shouldReturnEmptyListWhenNoData() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);

        when(repository.getPeriodReports(1, start, end))
                .thenReturn(List.of());

        List<PeriodReportsByHospitalResponse> result = useCase.execute(1, start, end);

        assertTrue(result.isEmpty());
    }
}
