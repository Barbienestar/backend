package com.itesm.application.usecase;

import java.util.Optional;

import com.itesm.domain.models.MedicinesHospitalsStockReport;
import com.itesm.domain.repository.MedicinesHospitalsRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetStockReportByHospitalUseCase {
    private final MedicinesHospitalsRepository repository;
    
    @Inject
    public GetStockReportByHospitalUseCase(MedicinesHospitalsRepository repository) {
        this.repository = repository;
    }

    public Optional<MedicinesHospitalsStockReport> execute(Integer idHospital) {
        return repository.getStockReport(idHospital);
    }
}
