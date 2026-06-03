package com.itesm.application.usecase;

import com.itesm.application.dto.MedicineDto;
import com.itesm.domain.repository.MedicineRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

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
                        m.getId(), m.getGenericName(), m.getDosageForm(), m.getStrength(), m.getPresentation()))
                .toList();
    }

    public List<MedicineDto> search(String query) {
        return medicineRepository.searchMedicines(query).stream()
                .map(this::toDto)
                .toList();
    }

    private MedicineDto toDto(com.itesm.domain.models.Medicine m) {
        return new MedicineDto(m.getId(), m.getGenericName(), m.getDosageForm(), m.getStrength(), m.getPresentation());
    }
}
