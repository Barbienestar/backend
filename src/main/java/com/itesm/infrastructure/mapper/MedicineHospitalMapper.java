package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.MedicineHospital;
import com.itesm.infrastructure.persistence.entity.MedicineHospitalEntity;

public class MedicineHospitalMapper {

    public static MedicineHospital toDomain(MedicineHospitalEntity entity) {
        return new MedicineHospital(
                entity.getId(),
                MedicineMapper.toDomain(entity.getMedicine()),
                HospitalMapper.toDomain(entity.getHospital()),
                entity.getStock(),
                entity.getEntryDate()
        );
    }

    public static MedicineHospitalEntity toEntity(MedicineHospital domain) {
        MedicineHospitalEntity entity = new MedicineHospitalEntity();
        entity.setId(domain.getId());
        entity.setMedicine(MedicineMapper.toEntity(domain.getMedicine()));
        entity.setHospital(HospitalMapper.toEntity(domain.getHospital()));
        entity.setStock(domain.getStock());
        entity.setEntryDate(domain.getEntryDate());
        return entity;
    }
}