package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.State;

import com.itesm.infrastructure.persistence.entity.StateEntity;

public class StateMapper {
    public static State toDomain(StateEntity entity) {
        return new State(
            entity.getId(),
            entity.getName()
        );
    }
    
}
