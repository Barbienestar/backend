package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.MedicinesHospitals;
import com.itesm.domain.models.MedicinesHospitalsStock;
import com.itesm.domain.models.MedicinesHospitalsStockAverages;
import com.itesm.domain.models.MedicinesHospitalsStockReport;
import com.itesm.domain.repository.MedicinesHospitalsRepository;
import com.itesm.infrastructure.mapper.MedicinesHospitalsMapper;
import com.itesm.infrastructure.persistence.entity.MedicinesHospitalsEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class MedicinesHospitalsRepositoryImpl
        implements MedicinesHospitalsRepository, PanacheRepositoryBase<MedicinesHospitalsEntity, Long> {
    @Inject
    EntityManager em;

    @Override
    public List<MedicinesHospitalsStock> findByMedicineName(String query) {
        List<Object[]> rows = em.createQuery(
                        """
                                SELECT h.id, h.name,
                                       CONCAT(s.name, ', ', sub.name, ', ', c.name),
                                       mh.stock, h.mapsUrl
                                FROM MedicinesHospitalsEntity mh
                                JOIN mh.medicine m
                                JOIN mh.hospital h
                                JOIN h.street s
                                JOIN s.idSuburb sub
                                JOIN sub.idCity c
                                WHERE LOWER(CONCAT(m.genericName, ' ', m.dosageForm, ' ', COALESCE(m.strength, '')))
                                      LIKE LOWER(CONCAT('%', :query, '%'))
                                ORDER BY mh.stock DESC
                                """,
                        Object[].class)
                .setParameter("query", query)
                .getResultList();

        List<MedicinesHospitalsStock> out = new ArrayList<MedicinesHospitalsStock>();
        for (Object[] r : rows) {
            out.add(new MedicinesHospitalsStock(
                    (Integer) r[0], (String) r[1], (String) r[2], (Integer) r[3], (String) r[4]));
        }

        return out;
    }

    @Override
    public Optional<MedicinesHospitalsStockAverages> getStockAvg(Integer idHospital) {
        Query query = em.createNativeQuery("CALL get_hospital_stock_averages(:idHospital)")
                .setParameter("idHospital", idHospital);
        Object[] row = (Object[]) query.getSingleResult();

        return Optional.of(new MedicinesHospitalsStockAverages((BigDecimal) row[0], (BigDecimal) row[1]));
    }

    @Override
    public Optional<MedicinesHospitalsStockReport> getStockReport(Integer idHospital) {
        Query query = em.createNativeQuery("CALL get_hospital_stock_report(:idHospital)")
                .setParameter("idHospital", idHospital);
        Object[] row = (Object[]) query.getSingleResult();

        if (row == null) return Optional.empty();

        Integer count = ((Number) row[0]).intValue();

        String medicinesString = (String) row[1];
        List<String> medicinesList = (medicinesString != null && !medicinesString.isEmpty())
                ? java.util.Arrays.asList(medicinesString.split(", "))
                : java.util.Collections.emptyList();

        return Optional.of(new MedicinesHospitalsStockReport(count, medicinesList));
    }

    @Override
    public List<Object[]> getPeriodStock(Integer idHospital, LocalDate startDate, LocalDate endDate) {
        return em.createQuery(
                        """
                                SELECT CAST(mh.entryDate AS date), SUM(mh.stock)
                                FROM MedicinesHospitalsEntity mh
                                WHERE mh.hospital.id = :idHospital
                                  AND CAST(mh.entryDate AS date) BETWEEN :startDate AND :endDate
                                GROUP BY CAST(mh.entryDate AS date)
                                ORDER BY CAST(mh.entryDate AS date) ASC
                                """,
                        Object[].class)
                .setParameter("idHospital", idHospital)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    @Override
    @Transactional
    public void saveAll(List<MedicinesHospitals> records) {
        records.stream().map(MedicinesHospitalsMapper::toEntity).forEach(this::persist);
    }

    @Override
    @Transactional
    public void save(MedicinesHospitals medicineHospital) {
        persist(MedicinesHospitalsMapper.toEntity(medicineHospital));
    }
}
