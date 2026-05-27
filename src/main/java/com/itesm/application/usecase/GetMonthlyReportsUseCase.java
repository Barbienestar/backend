package com.itesm.application.usecase;

import java.util.Optional;

import com.itesm.application.dto.MonthlyReportsResponse;
import com.itesm.domain.repository.MedicinesHospitalsRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
