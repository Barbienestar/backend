package com.itesm.application.usecase;

import com.itesm.application.dto.StateSupplyDto;
import com.itesm.domain.models.StateSupplyData;
import com.itesm.domain.repository.MedicinesHospitalsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class GetStateSupplyHeatmapUseCase {

    private final MedicinesHospitalsRepository repository;

    @Inject
    public GetStateSupplyHeatmapUseCase(MedicinesHospitalsRepository repository) {
        this.repository = repository;
    }

    public List<StateSupplyDto> execute() {
        return repository.findAvgStockByState().stream()
                .map(this::toDto)
                .toList();
    }

    private StateSupplyDto toDto(StateSupplyData data) {
        String level = resolveLevel(data.getAvgStock());
        return new StateSupplyDto(data.getStateId(), data.getStateName(), data.getAvgStock(), level);
    }

    private String resolveLevel(Double avgStock) {
        if (avgStock == null) return "CA-04";
        if (avgStock >= 75) return "CA-01";
        if (avgStock >= 50) return "CA-02";
        if (avgStock >= 25) return "CA-03";
        return "CA-04";
    }
}
