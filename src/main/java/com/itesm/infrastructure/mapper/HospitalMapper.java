package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Hospital;
import com.itesm.infrastructure.persistence.entity.HospitalEntity;

public class HospitalMapper {
    public static Hospital toDomain(HospitalEntity entity) {
        return new Hospital(
                entity.getId(),
                entity.getName(),
                entity.getMapsUrl()
        );
    }

    public static HospitalEntity toEntity(Hospital domain) {
        HospitalEntity entity = new HospitalEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setMapsUrl(domain.getMapsUrl());
        return entity;
    }
}