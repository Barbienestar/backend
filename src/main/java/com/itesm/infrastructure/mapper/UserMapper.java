package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.User;
import com.itesm.infrastructure.persistence.entity.RoleEntity;
import com.itesm.infrastructure.persistence.entity.SuburbEntity;
import com.itesm.infrastructure.persistence.entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setName(user.getName());
        entity.setLastName1(user.getLastName1());
        entity.setLastName2(user.getLastName2());
        entity.setAge(user.getAge());
        entity.setEmail(user.getEmail());
        entity.setProviderUuid(user.getProviderUuid());
        entity.setActive(user.isActive());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());

        if (entity.getRole() != null) {
            user.setRole(entity.getRole().getId());
        }
        if (entity.getSuburb() != null) {
            user.setSuburbId(entity.getSuburb().getId());
        }

        return entity;
    }

    public static User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setLastName1(entity.getLastName1());
        user.setLastName2(entity.getLastName2());
        user.setAge(entity.getAge());
        user.setEmail(entity.getEmail());
        user.setProviderUuid(entity.getProviderUuid());
        user.setActive(entity.isActive());
        user.setRole(entity.getRole() != null ? entity.getRole().getId() : null);
        user.setSuburbId(entity.getSuburb() != null ? entity.getSuburb().getId() : null);
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());
        return user;
    }
}