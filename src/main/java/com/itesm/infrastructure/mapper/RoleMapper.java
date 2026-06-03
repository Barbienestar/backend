package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Role;
import com.itesm.infrastructure.persistence.entity.RoleEntity;

public class RoleMapper {
    private RoleMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Role toDomain(RoleEntity entity) {
        if (entity == null) return null;
        return new Role(entity.getId(), entity.getName());
    }

    public static RoleEntity toEntity(Role role) {
        if (role == null) return null;
        return new RoleEntity(role.getId());
    }
}
