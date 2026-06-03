package com.itesm.infrastructure.firebase;

import com.itesm.domain.exceptions.InvalidTokenException;
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
    public TokenVerification verifyIdToken(String idToken) {
        return switch (idToken) {
            case "admin-token" -> new TokenVerification("admin-token", "admin@test.com", "Admin");
            case "health-token" -> new TokenVerification("health-token", "health@test.com", "Health");
            case "citizen-token" -> new TokenVerification("citizen-token", "citizen@test.com", "Citizen");
            default -> throw new InvalidTokenException("Invalid token", null);
        };
    }

    @Override
    public void deleteUser(String providerUuid) {
        return;
    }
}
