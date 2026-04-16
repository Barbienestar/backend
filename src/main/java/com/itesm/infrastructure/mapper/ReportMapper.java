package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Report;
import com.itesm.infrastructure.persistence.entity.ReportEntity;

public class ReportMapper {
    public static Report toDomain(ReportEntity entity) {
        return new Report(
                entity.getId(),
                entity.getUser().getId(),
                entity.getMedicine().getId(),
                entity.getHospital().getId(),
                entity.getStatusId().getId(),
                entity.getDescription(),
                entity.getImageUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}