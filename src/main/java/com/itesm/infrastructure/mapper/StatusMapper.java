package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Status;
import com.itesm.infrastructure.persistence.entity.StatusEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StatusMapper {
    private StatusMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Status toDomain(StatusEntity entity) {
        return new Status(entity.getId(), entity.getName());
    }
}
