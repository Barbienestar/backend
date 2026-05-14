package com.itesm.infrastructure.mapper;
import com.itesm.domain.models.Hospital;
import com.itesm.infrastructure.persistence.entity.UserHospitalEntity;

public class UserHospitalMapper {

    public static Hospital toDomain(UserHospitalEntity entity) {
        return HospitalMapper.toDomain(entity.getHospital());
    }
}
