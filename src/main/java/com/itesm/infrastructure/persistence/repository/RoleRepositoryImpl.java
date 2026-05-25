package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.Role;
import com.itesm.domain.repository.RoleRepository;
import com.itesm.infrastructure.mapper.RoleMapper;
import com.itesm.infrastructure.persistence.entity.RoleEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class RoleRepositoryImpl implements RoleRepository, PanacheRepositoryBase<RoleEntity, Byte> {

    @Override
    public Optional<Role> findByName(String name) {
        return find("name", name).firstResultOptional().map(RoleMapper::toDomain);
    }
}
