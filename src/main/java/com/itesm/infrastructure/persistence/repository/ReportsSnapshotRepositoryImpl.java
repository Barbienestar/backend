package com.itesm.infrastructure.persistence.repository;

import com.itesm.application.dto.PeriodReportsByHospitalResponse;
import com.itesm.domain.repository.ReportsSnapshotRepository;
import com.itesm.infrastructure.persistence.entity.ReportsSnapshotEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class ReportsSnapshotRepositoryImpl
        implements ReportsSnapshotRepository, PanacheRepositoryBase<ReportsSnapshotEntity, Long> {
    @Inject
    EntityManager em;

    @Override
    public List<PeriodReportsByHospitalResponse> getPeriodReports(
            Integer idHospital, LocalDate startDate, LocalDate endDate) {
        List<Object[]> rows = em.createQuery("""
            SELECT rs.snapshotDate, SUM(rs.dailyReports)
            FROM ReportsSnapshotEntity rs
            WHERE rs.hospital.id = :idHospital
              AND rs.snapshotDate BETWEEN :startDate AND :endDate
            GROUP BY rs.snapshotDate
            ORDER BY rs.snapshotDate ASC
            """, Object[].class)
                .setParameter("idHospital", idHospital)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();

        return rows.stream()
                .map(r -> new PeriodReportsByHospitalResponse((LocalDate) r[0], ((Number) r[1]).intValue()))
                .toList();
    }
}
