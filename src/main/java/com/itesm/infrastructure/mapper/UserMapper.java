package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.User;
import com.itesm.infrastructure.persistence.entity.UserEntity;

/**
 * UserMapper
 */
public class UserMapper {
    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setLastName1(user.getLastName1());
        entity.setLastName2(user.getLastName2());
        entity.setEmail(user.getEmail());
        entity.setProviderUid(user.getProviderUid());
        entity.setActive(user.isActive());
        entity.setRole(user.getRole());
        return entity;
    }

    public static User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setLastName1(entity.getLastName1());
        user.setLastName2(entity.getLastName2());
        user.setEmail(entity.getEmail());
        user.setProviderUid(entity.getProviderUid());
        user.setActive(entity.isActive());
        user.setRole(entity.getRole());
        return user;
    }
}
