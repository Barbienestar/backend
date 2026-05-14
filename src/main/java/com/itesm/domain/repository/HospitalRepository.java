package com.itesm.domain.repository;

import com.itesm.domain.models.Hospital;
import java.util.List;
import java.util.Optional;

public interface HospitalRepository {
    List<Hospital> findAllHospitals();
    Hospital findHospitalById(Integer id);
    List<Hospital> findHospitalsByUserId(Long userId);
}