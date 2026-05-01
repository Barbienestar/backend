package com.itesm.infrastructure.mapper;

import com.itesm.domain.models.Address;
import com.itesm.domain.models.Role;
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
        RoleEntity roleEntity = entity.getRole();
        user.setRole(
                roleEntity != null ? new Role(roleEntity.getId(), roleEntity.getName()) : null);
        SuburbEntity suburbEntity = entity.getSuburb();
        // TODO: Join all address fields to create a complete address string
        user.setAddress(
                suburbEntity != null
                        ? new Address(suburbEntity.getName(), suburbEntity.getId())
                        : null);
        return user;
    }
}
