package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Report;
import com.itesm.infrastructure.persistence.entity.ReportEntity;

public class ReportMapper {
    public static Report toDomain(ReportEntity entity) {
        Report report = new Report();
        report.setId(entity.getId());
        report.setUserId(entity.getUser().getId());
        report.setMedicineId(entity.getMedicine().getId());
        report.setHospitalId(entity.getHospital().getId());
        report.setStatusId(entity.getStatusId().getId());
        report.setDescription(entity.getDescription());
        report.setImageUrl(entity.getImageUrl());
        report.setCreatedAt(entity.getCreatedAt());
        report.setUpdatedAt(entity.getUpdatedAt());
        return report;
    }

    public static Report toDomainFull(ReportEntity entity) {
        Report report = new Report();
        report.setId(entity.getId());
        report.setUserId(entity.getUser().getId());
        report.setMedicineId(entity.getMedicine().getId());
        report.setHospitalId(entity.getHospital().getId());
        report.setStatusId(entity.getStatusId().getId());
        report.setDescription(entity.getDescription());
        report.setImageUrl(entity.getImageUrl());
        report.setCreatedAt(entity.getCreatedAt());
        report.setUpdatedAt(entity.getUpdatedAt());
        report.setUser(UserMapper.toDomain(entity.getUser()));
        report.setMedicine(MedicineMapper.toDomain(entity.getMedicine()));
        report.setHospital(HospitalMapper.toDomain(entity.getHospital()));
        return report;
    }
}
