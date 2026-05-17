package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.MedicinesHospitals;
import com.itesm.infrastructure.persistence.entity.MedicinesHospitalsEntity;

public class MedicinesHospitalsMapper {

    public static MedicinesHospitals toDomain(MedicinesHospitalsEntity entity) {
        return new MedicinesHospitals(
                entity.getId(),
                MedicineMapper.toDomain(entity.getMedicine()),
                HospitalMapper.toDomain(entity.getHospital()),
                entity.getStock(),
                entity.getEntryDate()
        );
    }

    public static MedicinesHospitalsEntity toEntity(MedicinesHospitals domain) {
        MedicinesHospitalsEntity entity = new MedicinesHospitalsEntity();
        entity.setId(domain.getId());
        entity.setMedicine(MedicineMapper.toEntity(domain.getMedicine()));
        entity.setHospital(HospitalMapper.toEntity(domain.getHospital()));
        entity.setStock(domain.getStock());
        entity.setEntryDate(domain.getEntryDate());
        return entity;
    }
}