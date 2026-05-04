package com.itesm.domain.repository;

import java.util.List;

import com.itesm.domain.models.City;

public interface CityRepository {
    List<City> getCitiesByState(Byte idState);
}
