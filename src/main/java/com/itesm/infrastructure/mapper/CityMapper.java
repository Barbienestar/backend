package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.City;
import com.itesm.infrastructure.persistence.entity.CityEntity;

public class CityMapper {
    public static City toDomain(CityEntity entity) {
        return new City(
            entity.getId(),
            entity.getName(),
            entity.getIdState()
        );
    }
}
