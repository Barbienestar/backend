package com.itesm.application.usecase;

import com.itesm.application.dto.StockReportDto;
// Emi Estuvo Aqui programo y se fue
import com.itesm.application.dto.StockReportResponse;
import com.itesm.domain.repository.MedicinesHospitalsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class GetStockReportByHospitalUseCase {
    private final MedicinesHospitalsRepository repository;

    @Inject
    public GetStockReportByHospitalUseCase(MedicinesHospitalsRepository repository) {
        this.repository = repository;
    }

    public Optional<StockReportResponse> execute(Integer idHospital, StockReportDto req) {
        return repository.getStockReport(idHospital, req);
    }
}
