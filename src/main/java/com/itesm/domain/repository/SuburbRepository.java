package com.itesm.domain.repository;

import java.util.List;

import com.itesm.domain.models.Suburb;

public interface SuburbRepository {
    List<Suburb> getSuburbsByCity(Integer idCity);
}
