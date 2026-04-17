package com.itesm.infrastructure.persistence.repository;

import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.transaction.Transactional;
import java.util.UUID;

/**
 * UserRepositoryImpl
 */
public class UserRepositoryImpl implements UserRepository, PanacheRepositoryBase<User, UUID> {
    @Override
    @Transactional
    public User save(User user) {
    }
}
