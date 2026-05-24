package com.itesm.application.usecase;

import com.itesm.application.dto.PeriodReportsByHospitalResponse;
import com.itesm.application.dto.PeriodReportsByHospitalWithStockResponse;
import com.itesm.domain.repository.MedicinesHospitalsRepository;
import com.itesm.domain.repository.ReportsSnapshotRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class GetPeriodReportsByHospitalWithStockUseCase {
    private final ReportsSnapshotRepository reportsSnapshotRepository;
    private final MedicinesHospitalsRepository medicinesHospitalsRepository;

    @Inject
    public GetPeriodReportsByHospitalWithStockUseCase(
            ReportsSnapshotRepository reportsSnapshotRepository,
            MedicinesHospitalsRepository medicinesHospitalsRepository) {
        this.reportsSnapshotRepository = reportsSnapshotRepository;
        this.medicinesHospitalsRepository = medicinesHospitalsRepository;
    }

    public List<PeriodReportsByHospitalWithStockResponse> execute(
            Integer idHospital, LocalDate startDate, LocalDate endDate) {
        List<PeriodReportsByHospitalResponse> reports =
                reportsSnapshotRepository.getPeriodReports(idHospital, startDate, endDate);
        List<Object[]> stockRows = medicinesHospitalsRepository.getPeriodStock(idHospital, startDate, endDate);

        Map<LocalDate, Integer> stockByDate = new HashMap<>();
        for (Object[] row : stockRows) {
            LocalDate date =
                    row[0] instanceof java.sql.Date ? ((java.sql.Date) row[0]).toLocalDate() : (LocalDate) row[0];
            stockByDate.put(date, ((Number) row[1]).intValue());
        }

        return reports.stream()
                .map(r -> new PeriodReportsByHospitalWithStockResponse(
                        r.getDate(), r.getTotalAcceptedReports(), stockByDate.getOrDefault(r.getDate(), 0)))
                .toList();
    }
}
