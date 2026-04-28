package com.itesm.domain.repository;

import java.util.List;

import com.itesm.domain.models.State;

public interface StateRepository {
    List<State> selectAll();
}