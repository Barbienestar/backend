package com.itesm.domain.repository;

import com.itesm.application.dto.PeriodReportsByHospitalResponse;
import java.time.LocalDate;
import java.util.List;

public interface ReportsSnapshotRepository {
    List<PeriodReportsByHospitalResponse> getPeriodReports(Integer idHospital, LocalDate startDate, LocalDate endDate);
}
