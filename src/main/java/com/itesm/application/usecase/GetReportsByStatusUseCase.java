package com.itesm.application.usecase;

import com.itesm.application.dto.FullReportResponse;
import com.itesm.application.dto.PagedResult;
import com.itesm.domain.models.Report;
import com.itesm.domain.repository.ImageRepository;
import com.itesm.domain.repository.ReportRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/** GetReportsByStatusUseCase */
@ApplicationScoped
public class GetReportsByStatusUseCase {
    private final ReportRepository reportRepository;
    private final ImageRepository imageRepository;

    @Inject
    public GetReportsByStatusUseCase(
            ReportRepository reportRepository, ImageRepository imageRepository) {
        this.reportRepository = reportRepository;
        this.imageRepository = imageRepository;
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

        String signedUrl =
                imageRepository.generateSignedUrl(report.getImageUrl(), 30, TimeUnit.MINUTES);
        FullReportResponse response =
                new FullReportResponse(
                        report.getDescription(),
                        signedUrl,
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
