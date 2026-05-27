package com.itesm.application.usecase;

import com.itesm.application.dto.MonthlyReportsResponse;
import com.itesm.domain.repository.MedicinesHospitalsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class GetMonthlyReportsUseCase {
    private final MedicinesHospitalsRepository medicinesHospitalsRepository;

    @Inject
    public GetMonthlyReportsUseCase(MedicinesHospitalsRepository medicinesHospitalsRepository) {
        this.medicinesHospitalsRepository = medicinesHospitalsRepository;
    }

    public Optional<MonthlyReportsResponse> execute(Integer idHospital) {
        return medicinesHospitalsRepository.getMonthlyReports(idHospital);
    }
}
