package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.MedicineHospitalStock;
import com.itesm.domain.repository.MedicineHospitalStockRepository;
import com.itesm.infrastructure.mapper.MedicineHospitalStockMapper;
import com.itesm.infrastructure.persistence.entity.MedicinesHospitalsEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MedicineHospitalStockRepositoryImpl implements MedicineHospitalStockRepository, PanacheRepositoryBase<MedicinesHospitalsEntity, Long> {

    @Override
    public List<MedicineHospitalStock> findByMedicineName(String query) {
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
                .map(MedicineHospitalStockMapper::toDomain)
                .collect(Collectors.toList());
    }
}