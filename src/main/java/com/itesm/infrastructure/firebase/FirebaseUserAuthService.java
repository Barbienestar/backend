package com.itesm.infrastructure.firebase;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.itesm.domain.exceptions.EmailAlreadyExistsException;
import com.itesm.domain.repository.UserTokenService;
import io.quarkus.arc.profile.UnlessBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

@ApplicationScoped
@UnlessBuildProfile("test")
public class FirebaseUserAuthService implements UserTokenService {
    @Override
    public String createUser(String email, String password) {
        try {
            UserRecord.CreateRequest createRequest =
                    new UserRecord.CreateRequest().setEmail(email).setPassword(password);
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(createRequest);
            return userRecord.getUid();
        } catch (FirebaseAuthException e) {
            if (e.getMessage() != null && e.getMessage().contains("EMAIL_EXISTS")) {
                throw new EmailAlreadyExistsException(email);
            }
            throw new RuntimeException("Failed to create Firebase user: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(String providerUuid) {
        try {
            FirebaseAuth.getInstance().deleteUser(providerUuid);
        } catch (FirebaseException e) {
            // Log this so the orphaned Firebase user can be cleaned up manually
            Logger.getLogger(FirebaseUserAuthService.class.getName())
                    .severe("Rollback failed — orphaned Firebase user: " + providerUuid + " | " + e.getMessage());
            throw new RuntimeException("Failed to delete Firebase user during rollback: " + e.getMessage(), e);
        }
    }
}
