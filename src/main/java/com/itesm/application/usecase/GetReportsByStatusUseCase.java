package com.itesm.application.usecase;

import com.itesm.application.dto.PagedResult;
import com.itesm.domain.models.Report;
import com.itesm.domain.repository.ReportRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

/** GetReportsByStatusUseCase */
@ApplicationScoped
public class GetReportsByStatusUseCase {
    private final ReportRepository reportRepository;

    @Inject
    public GetReportsByStatusUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public PagedResult<Report> execute(Integer statusId, Integer page, Integer pageSize) {
        List<Report> reports = reportRepository.findByStatusId(statusId, page, pageSize);
        long totalItems = reportRepository.countByStatusId(statusId);

        Integer totalPages = (int) Math.ceil((double) totalItems / pageSize);
        return new PagedResult<>(reports, page, pageSize, totalItems, totalPages);
    }
}
