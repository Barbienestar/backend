package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import com.itesm.infrastructure.mapper.UserMapper;
import com.itesm.infrastructure.persistence.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.transaction.Transactional;
import java.util.UUID;

/**
 * UserRepositoryImpl
 */
public class UserRepositoryImpl implements UserRepository, PanacheRepositoryBase<UserEntity, UUID> {
    @Override
    @Transactional
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        persist(entity);
        return UserMapper.toDomain(entity);
    }
}
