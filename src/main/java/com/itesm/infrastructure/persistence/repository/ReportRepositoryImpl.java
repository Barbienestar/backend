package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.Report;
import com.itesm.domain.models.Status;
import com.itesm.domain.repository.ReportRepository;
import com.itesm.infrastructure.mapper.ReportMapper;
import com.itesm.infrastructure.persistence.entity.*;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReportRepositoryImpl
        implements ReportRepository, PanacheRepositoryBase<ReportEntity, Integer> {

    private final StatusRepositoryImpl statusRepository;
    private final EntityManager em;

    @Inject
    public ReportRepositoryImpl(StatusRepositoryImpl statusRepository, EntityManager em) {
        this.statusRepository = statusRepository;
        this.em = em;
    }

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

        Status status = statusRepository.findStatusByName("Pendiente");
        entity.setStatusId(new StatusEntity(status.getId()));

        entity.setDescription(report.getDescription());
        entity.setImageUrl(report.getImageUrl() != null ? report.getImageUrl() : "");
        entity.setCreatedAt(report.getCreatedAt());
        entity.setUpdatedAt(report.getUpdatedAt());

        persist(entity);
        return ReportMapper.toDomain(entity);
    }

    @Override
    public List<Report> findByUserId(Integer userId) {
        return find("user.id", userId).stream()
                .map(ReportMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> findByStatusId(Integer statusId, Integer page, Integer pageSize) {
        List<ReportEntity> entities =
                em.createQuery(
                                "SELECT r FROM ReportEntity r "
                                        + "JOIN FETCH r.user u "
                                        + "JOIN FETCH u.role "
                                        + "JOIN FETCH u.suburb "
                                        + "WHERE r.statusId.id = :statusId",
                                ReportEntity.class)
                        .setParameter("statusId", statusId)
                        .setHint(
                                "jakarta.persistence.loadgraph",
                                em.getEntityGraph("Report.withData"))
                        .setFirstResult(page * pageSize) // offset
                        .setMaxResults(pageSize)
                        .getResultList();

        return entities.stream().map(ReportMapper::toDomainFull).collect(Collectors.toList());
    }

    @Override
    public long countByStatusId(Integer statusId) {
        return em.createQuery(
                        "SELECT COUNT(r) FROM ReportEntity r WHERE r.statusId.id = :statusId",
                        Long.class)
                .setParameter("statusId", statusId)
                .getSingleResult();
    }
}
