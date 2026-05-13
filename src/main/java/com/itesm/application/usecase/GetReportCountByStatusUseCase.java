package com.itesm.application.usecase;

import com.itesm.domain.repository.ReportRepository;

import jakarta.inject.Inject;

/** GetReportCountByStatusUseCase */
public class GetReportCountByStatusUseCase {

    private final ReportRepository reportRepository;

    @Inject
    public GetReportCountByStatusUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public long execute(Integer statusId) {
        return reportRepository.countByStatusId(statusId);
    }
}
