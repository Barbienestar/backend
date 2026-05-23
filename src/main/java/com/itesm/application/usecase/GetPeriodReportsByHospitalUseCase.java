package com.itesm.application.usecase;

import com.itesm.application.dto.PeriodReportsByHospitalResponse;
import com.itesm.domain.repository.ReportsSnapshotRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class GetPeriodReportsByHospitalUseCase {
    private final ReportsSnapshotRepository repository;

    @Inject
    public GetPeriodReportsByHospitalUseCase(ReportsSnapshotRepository repository) {
        this.repository = repository;
    }

    public List<PeriodReportsByHospitalResponse> execute(
            Integer idHospital, LocalDate startDate, LocalDate endDate) {
        return repository.getPeriodReports(idHospital, startDate, endDate);
    }
}
