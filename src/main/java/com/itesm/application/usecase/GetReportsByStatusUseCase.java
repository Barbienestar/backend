package com.itesm.application.usecase;

import com.itesm.application.dto.FullReportResponse;
import com.itesm.application.dto.PagedResult;
import com.itesm.domain.models.Report;
import com.itesm.domain.repository.ReportRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

/** GetReportsByStatusUseCase */
@ApplicationScoped
public class GetReportsByStatusUseCase {
    private final ReportRepository reportRepository;

    @Inject
    public GetReportsByStatusUseCase(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public PagedResult<FullReportResponse> execute(
            Integer statusId, Integer page, Integer pageSize) {
        List<Report> reports = reportRepository.findByStatusId(statusId, page, pageSize);
        long totalItems = reportRepository.countByStatusId(statusId);

        Integer totalPages = (int) Math.ceil((double) totalItems / pageSize);
        List<FullReportResponse> fullReports =
                reports.stream().map(this::toFullReportResponse).collect(Collectors.toList());
        return new PagedResult<>(fullReports, page, pageSize, totalItems, totalPages);
    }

    private FullReportResponse toFullReportResponse(Report report) {
        FullReportResponse response =
                new FullReportResponse(
                        report.getDescription(),
                        report.getImageUrl(),
                        report.getUser().getName()
                                + " "
                                + report.getUser().getLastName1()
                                + " "
                                + report.getUser().getLastName2(),
                        report.getMedicine().getGenericName(),
                        report.getMedicine().getPresentation(),
                        report.getMedicine().getDosageForm(),
                        report.getHospital().getName());
        return response;
    }
}
