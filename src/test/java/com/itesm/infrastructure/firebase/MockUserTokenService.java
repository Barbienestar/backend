package com.itesm.infrastructure.firebase;

import com.itesm.domain.repository.UserTokenService;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class MockUserTokenService implements UserTokenService {

    @Override
    public String createUser(String email, String password) {
        return UUID.randomUUID().toString();
    }

    @Override
    public void deleteUser(String providerUuid) {
        return;
    }
}
