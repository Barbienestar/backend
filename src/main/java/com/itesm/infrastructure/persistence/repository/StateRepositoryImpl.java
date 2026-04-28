package com.itesm.infrastructure.persistence.repository;

import java.util.List;
import java.util.stream.Collectors;

import com.itesm.domain.models.State;
import com.itesm.domain.repository.StateRepository;
import com.itesm.infrastructure.mapper.StateMapper;
import com.itesm.infrastructure.persistence.entity.StateEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StateRepositoryImpl implements StateRepository, PanacheRepositoryBase<StateEntity, Byte>{
    @Override
    public List<State> selectAll() {
        return listAll().stream()
                .map(StateMapper::toDomain)
                .collect(Collectors.toList());
    }
}
