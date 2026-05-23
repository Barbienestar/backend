package com.itesm.domain.repository;

import com.itesm.domain.models.Role;
import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(String name);
}
