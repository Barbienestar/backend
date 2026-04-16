package com.itesm.application.usecase;

import com.itesm.application.dto.MedicineDto;
import com.itesm.domain.repository.MedicineRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetMedicinesUseCase {

    private final MedicineRepository medicineRepository;

    @Inject
    public GetMedicinesUseCase(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public List<MedicineDto> execute() {
        return medicineRepository.findAllMedicines().stream()
                .map(m -> new MedicineDto(
                        m.getId(),
                        m.getGenericName(),
                        m.getDosageForm(),
                        m.getStrength(),
                        m.getPresentation()
                ))
                .collect(Collectors.toList());
    }
}