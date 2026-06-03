package com.itesm.application.usecase;

import com.itesm.application.dto.CriticalMedicineDto;
import com.itesm.application.dto.PagedHospitalCriticalMedicinesDto;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.domain.models.Hospital;
import com.itesm.domain.models.MedicinesHospitals;
import com.itesm.domain.repository.HospitalRepository;
import com.itesm.domain.repository.MedicinesHospitalsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class GetCriticalMedicinesUseCase {

    private static final int CRITICAL_STOCK_THRESHOLD = 9;

    private final MedicinesHospitalsRepository medicinesHospitalsRepository;
    private final HospitalRepository hospitalRepository;
    private final AuthenticatedUserContext authUserContext;

    @Inject
    public GetCriticalMedicinesUseCase(
            MedicinesHospitalsRepository medicinesHospitalsRepository,
            HospitalRepository hospitalRepository,
            AuthenticatedUserContext authUserContext) {
        this.medicinesHospitalsRepository = medicinesHospitalsRepository;
        this.hospitalRepository = hospitalRepository;
        this.authUserContext = authUserContext;
    }

    public PagedHospitalCriticalMedicinesDto execute(Integer idHospital, int page, int size) {
        Long userId = authUserContext.getCurrentUser().getId();

        boolean hospitalBelongsToUser = hospitalRepository.findHospitalsByUserId(userId).stream()
                .anyMatch(h -> h.getId().equals(idHospital));

        if (!hospitalBelongsToUser) {
            throw new RuntimeException("Hospital no asignado al usuario");
        }

        List<MedicinesHospitals> latestReports =
                medicinesHospitalsRepository.findLatestReportsByHospitalIds(List.of(idHospital), page, size);

        long totalElements = medicinesHospitalsRepository.countCriticalByHospitalIds(List.of(idHospital));
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<CriticalMedicineDto> criticalMedicines = latestReports.stream()
                .filter(r -> r.getStock() <= CRITICAL_STOCK_THRESHOLD)
                .map(r -> new CriticalMedicineDto(
                        r.getMedicine().getId(), r.getMedicine().getGenericName(), r.getStock()))
                .toList();

        Hospital hospital = latestReports.isEmpty()
                ? hospitalRepository.findHospitalsByUserId(userId).stream()
                        .filter(h -> h.getId().equals(idHospital))
                        .findFirst()
                        .orElseThrow()
                : latestReports.get(0).getHospital();

        return new PagedHospitalCriticalMedicinesDto(
                hospital.getId(), hospital.getName(), criticalMedicines, totalElements, totalPages, page);
    }
}
