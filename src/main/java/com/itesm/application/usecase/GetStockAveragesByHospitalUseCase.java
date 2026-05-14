package com.itesm.application.usecase;

import java.util.Optional;

import com.itesm.domain.models.MedicinesHospitalsStockAverages;
import com.itesm.domain.repository.MedicinesHospitalsRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GetStockAveragesByHospitalUseCase {
    private final MedicinesHospitalsRepository repository;

    @Inject
    public GetStockAveragesByHospitalUseCase(MedicinesHospitalsRepository repository) {
        this.repository = repository;
    }

    public Optional<MedicinesHospitalsStockAverages> execute(Integer idHospital) {
        return repository.getStockAvg(idHospital);
    }
}
