package com.itesm.domain.repository;

import com.itesm.domain.models.User;

import java.util.Optional;

/** UserRepository */
public interface UserRepository {
    User save(User user);

    Optional<User> findByProviderUuid(String providerUuid);

    Optional<User> findDomainById(Long id);
}
