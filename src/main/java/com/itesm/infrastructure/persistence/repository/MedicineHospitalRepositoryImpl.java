package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.MedicineHospital;
import com.itesm.infrastructure.mapper.MedicineHospitalMapper;
import com.itesm.infrastructure.persistence.entity.MedicineHospitalEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.transaction.Transactional;
import java.util.List;
import com.itesm.domain.repository.MedicineHospitalRepository;


public class MedicineHospitalRepositoryImpl implements MedicineHospitalRepository, PanacheRepositoryBase<MedicineHospitalEntity, Integer> {


    @Override
    @Transactional
    public void saveAll(List<MedicineHospital> records) {
        records.stream()
                .map(MedicineHospitalMapper::toEntity)
                .forEach(this::persist);
    }

    @Override
    @Transactional
    public void save(MedicineHospital medicineHospital) {
        persist(MedicineHospitalMapper.toEntity(medicineHospital));
    }
}