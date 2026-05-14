package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.Hospital;
import com.itesm.domain.models.Medicine;
import com.itesm.domain.repository.HospitalRepository;
import com.itesm.infrastructure.mapper.HospitalMapper;
import com.itesm.infrastructure.mapper.UserHospitalMapper;
import com.itesm.infrastructure.persistence.entity.HospitalEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class HospitalRepositoryImpl implements HospitalRepository, PanacheRepositoryBase<HospitalEntity, Integer> {

    @Override
    public List<Hospital> findAllHospitals() {
        return listAll().stream()
                .map(HospitalMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Hospital findHospitalById(Integer id) {
        HospitalEntity hospitalEntity = findById(id);
        if (hospitalEntity == null) {
            return  null;
        }
        return HospitalMapper.toDomain(hospitalEntity);
    }

    @Inject
    UserHospitalPanacheRepository userHospitalPanacheRepository;
    
    @Override
    public List<Hospital> findHospitalsByUserId(Long userId) {
        return userHospitalPanacheRepository.list("id.idUser", userId)
                .stream()
                .map(UserHospitalMapper::toDomain)
                .toList();
    }
}