package com.itesm.domain.repository;

import com.itesm.domain.models.User;

/**
 * UserRepository
 */
public interface UserRepository {
    User save(User user);
}
