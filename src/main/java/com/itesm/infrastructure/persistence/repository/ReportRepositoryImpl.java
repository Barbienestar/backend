package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.Report;
import com.itesm.domain.models.Status;
import com.itesm.domain.repository.HospitalRepository;
import com.itesm.domain.repository.MedicineRepository;
import com.itesm.domain.repository.ReportRepository;
import com.itesm.infrastructure.mapper.ReportMapper;
import com.itesm.infrastructure.persistence.entity.*;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReportRepositoryImpl implements ReportRepository, PanacheRepositoryBase<ReportEntity, Integer> {

    @Inject
    StatusRepositoryImpl statusRepository;

    @Override
    @Transactional
    public Report save(Report report) {
        ReportEntity entity = new ReportEntity();

        UserEntity user = new UserEntity();
        user.setId(report.getUserId());
        entity.setUser(user);

        MedicineEntity medicine = new MedicineEntity();
        medicine.setId(report.getMedicineId());
        entity.setMedicine(medicine);

        HospitalEntity hospital = new HospitalEntity();
        hospital.setId(report.getHospitalId());
        entity.setHospital(hospital);

        Status status = statusRepository.findStatusByName("reviewing");
        entity.setStatusId(new StatusEntity(status.getId()));

        entity.setDescription(report.getDescription());
        entity.setImageUrl(report.getImageUrl() != null ? report.getImageUrl() : "");
        entity.setCreatedAt(report.getCreatedAt());
        entity.setUpdatedAt(report.getUpdatedAt());

        persist(entity);
        return ReportMapper.toDomain(entity);
    }

    @Override
    public List<Report> findByUserId(Long userId) {
        return find("user.id", userId).stream()
                .map(ReportMapper::toDomain)
                .collect(Collectors.toList());
    }
}