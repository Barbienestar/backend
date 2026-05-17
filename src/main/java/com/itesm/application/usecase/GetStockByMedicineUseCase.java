package com.itesm.application.usecase;

import com.itesm.application.dto.MedicinesHospitalsStockDto;
import com.itesm.domain.models.MedicinesHospitalsStock;
import com.itesm.domain.repository.MedicinesHospitalsRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetStockByMedicineUseCase {

    private final MedicinesHospitalsRepository repository;

    @Inject
    public GetStockByMedicineUseCase(MedicinesHospitalsRepository repository) {
        this.repository = repository;
    }

    public List<MedicinesHospitalsStockDto> execute(String medicineName) {
        return repository.findByMedicineName(medicineName).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private MedicinesHospitalsStockDto toDto(MedicinesHospitalsStock item) {
        return new MedicinesHospitalsStockDto(
                item.getHospitalId(),
                item.getHospitalName(),
                item.getAddress(),
                resolveStockLabel(item.getStock()),
                resolveStatus(item.getStock()),
                item.getMapsUrl()
        );
    }

    private String resolveStockLabel(int stock) {
        if (stock == 0)  return "No disponible";
        if (stock <= 10) return stock + " piezas restantes";
        return "Stock Alto";
    }

    private String resolveStatus(int stock) {
        if (stock == 0)  return "Agotado";
        if (stock <= 10) return "Limitado";
        return "Disponible";
    }
}