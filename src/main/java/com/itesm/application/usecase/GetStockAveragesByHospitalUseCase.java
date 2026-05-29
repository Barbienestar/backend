package com.itesm.application.usecase;

import com.itesm.application.dto.StockAveragesResponse;
import com.itesm.domain.repository.MedicinesHospitalsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class GetStockAveragesByHospitalUseCase {
    private final MedicinesHospitalsRepository repository;

    @Inject
    public GetStockAveragesByHospitalUseCase(MedicinesHospitalsRepository repository) {
        this.repository = repository;
    }

    public Optional<StockAveragesResponse> execute(Integer idHospital) {
        return repository.getStockAvg(idHospital);
    }
}
