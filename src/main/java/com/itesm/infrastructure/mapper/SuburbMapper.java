package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Suburb;
import com.itesm.infrastructure.persistence.entity.SuburbEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SuburbMapper {
    private SuburbMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Suburb toDomain(SuburbEntity entity) {
        return new Suburb(
                entity.getId(),
                entity.getName(),
                entity.getZipCode(),
                entity.getIdCity().getId());
    }
}
