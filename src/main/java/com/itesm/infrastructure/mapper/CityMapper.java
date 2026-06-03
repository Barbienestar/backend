package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.City;
import com.itesm.infrastructure.persistence.entity.CityEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CityMapper {
    public static City toDomain(CityEntity entity) {
        return new City(entity.getId(), entity.getName(), entity.getIdState().getId());
    }
}
