package com.itesm.application.usecase;

import com.itesm.application.dto.CreateReportDto;
import com.itesm.application.dto.ReportDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.domain.models.Report;
import com.itesm.domain.repository.HospitalRepository;
import com.itesm.domain.repository.MedicineRepository;
import com.itesm.domain.repository.ReportRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;

@ApplicationScoped
public class CreateReportUseCase {
    private final ReportRepository reportRepository;
    private final AuthenticatedUserContext authUserContext;
    private final HospitalRepository hospitalRepository;
    private final MedicineRepository medicineRepository;

    @Inject
    public CreateReportUseCase(ReportRepository reportRepository,
        AuthenticatedUserContext authUserContext, HospitalRepository hospitalRepository,
        MedicineRepository medicineRepository) {
        this.reportRepository = reportRepository;
        this.authUserContext = authUserContext;
        this.hospitalRepository = hospitalRepository;
        this.medicineRepository = medicineRepository;
    }

    public ReportDto execute(CreateReportDto dto) {
        LocalDateTime now = LocalDateTime.now();

        Report report =
            new Report(null, authUserContext.getCurrentUser().getId(), dto.getMedicineId(),
                dto.getHospitalId(), null, dto.getDescription(), dto.getImageUrl(), now, now);

        Report saved = reportRepository.save(report);
        String medicineName =
            medicineRepository.findMedicineById(saved.getMedicineId()).getGenericName();
        String hospitalName = hospitalRepository.findHospitalById(saved.getHospitalId()).getName();

        return new ReportDto(saved.getId(), medicineName, hospitalName, saved.getStatusId(),
            saved.getDescription(), saved.getCreatedAt());
    }
}
