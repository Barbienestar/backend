package com.itesm.domain.repository;

import com.itesm.domain.models.Suburb;
import java.util.List;

public interface SuburbRepository {
    List<Suburb> getSuburbsByCity(Integer idCity);
}
