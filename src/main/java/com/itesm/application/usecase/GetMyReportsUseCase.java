package com.itesm.application.usecase;

import com.itesm.application.dto.ReportSummaryDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.domain.models.Report;
import com.itesm.domain.models.Status;
import com.itesm.domain.repository.HospitalRepository;
import com.itesm.domain.repository.MedicineRepository;
import com.itesm.domain.repository.ReportRepository;
import com.itesm.domain.repository.StatusRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetMyReportsUseCase {

    private final ReportRepository reportRepository;
    private final MedicineRepository medicineRepository;
    private final HospitalRepository hospitalRepository;
    private final StatusRepository statusRepository;
    private final AuthenticatedUserContext authUserContext;

    @Inject
    public GetMyReportsUseCase(
            ReportRepository reportRepository,
            MedicineRepository medicineRepository,
            HospitalRepository hospitalRepository,
            StatusRepository statusRepository,
            AuthenticatedUserContext authUserContext) {
        this.reportRepository = reportRepository;
        this.medicineRepository = medicineRepository;
        this.hospitalRepository = hospitalRepository;
        this.statusRepository = statusRepository;
        this.authUserContext = authUserContext;
    }

    public List<ReportSummaryDto> execute() {
        Long userId = authUserContext.getCurrentUser().getId();

        List<Report> reports = reportRepository.findByUserId(userId);

        return reports.stream().map(report -> {
            String medicineName = medicineRepository.findMedicineById(report.getMedicineId()).getGenericName();
            String hospitalName = hospitalRepository.findHospitalById(report.getHospitalId()).getName();
            Status status = statusRepository.findStatusById(report.getStatusId());
            String statusName = status != null ? status.getName() : "unknown";

            return new ReportSummaryDto(
                    report.getId(),
                    medicineName,
                    hospitalName,
                    statusName,
                    report.getDescription(),
                    report.getImageUrl(),
                    report.getCreatedAt(),
                    report.getUpdatedAt()
            );
        }).collect(Collectors.toList());
    }
}
