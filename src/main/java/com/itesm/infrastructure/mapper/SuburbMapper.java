package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Suburb;
import com.itesm.infrastructure.persistence.entity.SuburbEntity;

public class SuburbMapper {
    public static Suburb toDomain(SuburbEntity entity) {
        return new Suburb(
            entity.getId(),
            entity.getName(),
            entity.getZipCode(),
            entity.getIdCity()
        );
    }
    
}
