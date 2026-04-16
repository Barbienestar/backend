package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Status;
import com.itesm.infrastructure.persistence.entity.StatusEntity;

public class StatusMapper {
    public static Status toDomain(StatusEntity entity) {
        return new Status(
                entity.getId(),
                entity.getName()
        );
    }
}