package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.Medicine;
import com.itesm.domain.repository.MedicineRepository;
import com.itesm.infrastructure.mapper.MedicineMapper;
import com.itesm.infrastructure.persistence.entity.MedicineEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class MedicineRepositoryImpl implements MedicineRepository, PanacheRepositoryBase<MedicineEntity, Integer> {

    @Override
    public List<Medicine> findAllMedicines() {
        return listAll().stream()
                .map(MedicineMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Medicine findMedicineById(Integer id) {
        MedicineEntity entity = findByIdOptional(id).orElse(null);
        if (entity == null) {
            return null;
        }
        return MedicineMapper.toDomain(entity);
    }

    @Override
    public List<Medicine> searchMedicines(String query) {
        return find("""
            SELECT m FROM MedicineEntity m
            WHERE LOWER(CONCAT(m.genericName, ' ', m.dosageForm, ' ', COALESCE(m.strength, '')))
                  LIKE LOWER(CONCAT('%', ?1, '%'))
            ORDER BY m.genericName ASC
            """, query)
                .stream()
                .map(MedicineMapper::toDomain)
                .collect(Collectors.toList());
    }
}