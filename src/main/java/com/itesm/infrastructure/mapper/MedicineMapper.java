package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Medicine;
import com.itesm.infrastructure.persistence.entity.MedicineEntity;

public class MedicineMapper {
    public static Medicine toDomain(MedicineEntity entity) {
        return new Medicine(
                entity.getId(),
                entity.getGenericName(),
                entity.getDosageForm(),
                entity.getStrength(),
                entity.getPresentation()
        );
    }

    public static MedicineEntity toEntity(Medicine domain) {
        MedicineEntity entity = new MedicineEntity();
        entity.setId(domain.getId());
        entity.setGenericName(domain.getGenericName());
        entity.setDosageForm(domain.getDosageForm());
        entity.setStrength(domain.getStrength());
        entity.setPresentation(domain.getPresentation());
        return entity;
    }
}