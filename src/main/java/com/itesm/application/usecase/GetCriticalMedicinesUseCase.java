package com.itesm.application.usecase;

import com.itesm.application.dto.CriticalMedicineDto;
import com.itesm.application.dto.HospitalCriticalMedicinesDto;
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

    private static final int CRITICAL_STOCK_THRESHOLD = 100;

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

    public List<HospitalCriticalMedicinesDto> execute() {
        Long userId = authUserContext.getCurrentUser().getId();

        List<Hospital> myHospitals = hospitalRepository.findHospitalsByUserId(userId);

        List<Integer> hospitalIds = myHospitals.stream().map(Hospital::getId).toList();

        List<MedicinesHospitals> latestReports =
                medicinesHospitalsRepository.findLatestReportsByHospitalIds(hospitalIds);

        return myHospitals.stream()
                .map(hospital -> {
                    List<CriticalMedicineDto> criticalMedicines = latestReports.stream()
                            .filter(r -> r.getHospital().getId().equals(hospital.getId()))
                            .filter(r -> r.getStock() <= CRITICAL_STOCK_THRESHOLD)
                            .map(r -> new CriticalMedicineDto(
                                    r.getMedicine().getId(), r.getMedicine().getGenericName(), r.getStock()))
                            .toList();
                    return new HospitalCriticalMedicinesDto(hospital.getId(), hospital.getName(), criticalMedicines);
                })
                .filter(h -> !h.getCriticalMedicines().isEmpty())
                .toList();
    }
}
