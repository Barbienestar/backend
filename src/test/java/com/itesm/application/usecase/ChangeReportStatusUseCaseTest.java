package com.itesm.application.usecase;

import static org.mockito.Mockito.*;

import com.itesm.domain.repository.ReportRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** ChangeReportStatusUseCaseTest */
public class ChangeReportStatusUseCaseTest {
    private ReportRepository reportRepository;
    private ChangeReportStatusUseCase changeReportStatusUseCase;

    @BeforeEach
    void setup() {
        reportRepository = mock(ReportRepository.class);
        changeReportStatusUseCase = new ChangeReportStatusUseCase(reportRepository);
    }

    @Test
    void execute_shouldCallChangeStatus() {
        changeReportStatusUseCase.execute(1, 2);
        verify(reportRepository, times(1)).changeStatus(1, 2);
    }
}
