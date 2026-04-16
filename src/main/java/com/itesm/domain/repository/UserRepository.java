package com.itesm.domain.repository;

import com.itesm.domain.models.User;
import java.util.Optional;
import java.util.UUID;

/**
 * UserRepository
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findByProviderUuid(String providerUuid);
    Optional<User> findDomainById(Integer id);
}
