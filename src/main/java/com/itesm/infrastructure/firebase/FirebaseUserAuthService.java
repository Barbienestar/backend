package com.itesm.infrastructure.firebase;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.itesm.domain.repository.UserTokenService;
import jakarta.enterprise.context.ApplicationScoped;
/**
 * FirebaseUserAuthService
 */
@ApplicationScoped
public class FirebaseUserAuthService implements UserTokenService {
    @Override
    public String createUser(String email, String password) {
        try {
            UserRecord.CreateRequest createRequest =
                new UserRecord.CreateRequest().setEmail(email).setPassword(password);
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(createRequest);
            return userRecord.getUid();
        } catch (FirebaseException e) {
            throw new RuntimeException("Failed to create Firebase user: " + e.getMessage(), e);
        }
    }
}
