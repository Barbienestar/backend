package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.State;
import com.itesm.infrastructure.persistence.entity.StateEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StateMapper {
    private StateMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static State toDomain(StateEntity entity) {
        return new State(entity.getId(), entity.getName());
    }
}
