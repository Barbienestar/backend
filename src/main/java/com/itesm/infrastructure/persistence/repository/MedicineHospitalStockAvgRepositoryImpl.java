package com.itesm.infrastructure.persistence.repository;

import java.math.BigDecimal;
import java.util.Optional;

import com.itesm.domain.models.MedicinesHospitalsStockAverages;
import com.itesm.domain.repository.MedicineHospitalStockAvgRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@ApplicationScoped
public class MedicineHospitalStockAvgRepositoryImpl implements MedicineHospitalStockAvgRepository, PanacheRepositoryBase<MedicinesHospitalsStockAverages, Long> {
    @Inject
    EntityManager em;

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
