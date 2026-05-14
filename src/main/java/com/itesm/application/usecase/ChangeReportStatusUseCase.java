package com.itesm.application.usecase;

import com.itesm.domain.repository.ReportRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/** ChangeReportStatusUseCase */
@ApplicationScoped
public class ChangeReportStatusUseCase {

    private final ReportRepository reportRepository;

    @Inject
    public ChangeReportStatusUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void execute(Integer reportId, Integer statusId) {
        reportRepository.changeStatus(reportId, statusId);
    }
}
