package com.itesm.domain.repository;

import com.itesm.domain.models.City;
import java.util.List;

public interface CityRepository {
    List<City> getCitiesByState(Byte idState);
}
