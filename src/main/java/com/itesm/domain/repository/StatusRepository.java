package com.itesm.domain.repository;

import com.itesm.domain.models.Status;
import com.itesm.infrastructure.persistence.entity.StatusEntity;

import java.util.List;

public interface StatusRepository {
    Status findStatusByName(String name);
}
