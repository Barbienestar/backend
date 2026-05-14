package com.itesm.domain.repository;

import com.itesm.domain.models.Status;

import java.util.List;

public interface StatusRepository {
    Status findStatusByName(String name);

    Status findStatusById(Byte id);

    List<Status> getAll();
}
