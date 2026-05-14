package com.itesm.infrastructure.persistence.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.itesm.domain.models.MedicinesHospitalsStock;
import com.itesm.domain.models.MedicinesHospitalsStockAverages;
import com.itesm.domain.repository.MedicinesHospitalsRepository;
import com.itesm.infrastructure.mapper.MedicinesHospitalsStockMapper;
import com.itesm.infrastructure.persistence.entity.MedicinesHospitalsEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@ApplicationScoped
public class MedicinesHospitalsRepositoryImpl implements MedicinesHospitalsRepository, PanacheRepositoryBase<MedicinesHospitalsEntity, Long> {
    @Inject EntityManager em;

    @Override
    public List<MedicinesHospitalsStock> findByMedicineName(String query) {
        return find("""
                SELECT mh FROM MedicinesHospitalsEntity mh
                JOIN FETCH mh.medicine m
                JOIN FETCH mh.hospital h
                JOIN FETCH h.street s
                JOIN FETCH s.idSuburb sub
                JOIN FETCH sub.idCity c
                WHERE LOWER(CONCAT(m.genericName, ' ', m.dosageForm, ' ', COALESCE(m.strength, '')))
                      LIKE LOWER(CONCAT('%', ?1, '%'))
                ORDER BY mh.stock DESC
                """, query)
                .stream()
                .map(MedicinesHospitalsStockMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MedicinesHospitalsStockAverages> getStockAvg(Integer idHospital) {
        Query query = em.createNativeQuery("CALL GetHospitalStockAverages(:idHospital)")
                .setParameter("idHospital", idHospital);
        Object[] row = (Object[]) query.getSingleResult();

        return Optional.of(new MedicinesHospitalsStockAverages(
            (BigDecimal) row[0],
            (BigDecimal) row[1]
        ));
    }
}
