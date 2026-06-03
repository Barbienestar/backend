package com.itesm.infrastructure.firebase;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.itesm.domain.exceptions.EmailAlreadyExistsException;
import com.itesm.domain.exceptions.FirebaseUserCreationException;
import com.itesm.domain.exceptions.FirebaseUserDeletionException;
import com.itesm.domain.exceptions.InvalidTokenException;
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
            throw new FirebaseUserCreationException("Failed to create Firebase user: " + e.getMessage(), e);
        }
    }

    @Override
    public TokenVerification verifyIdToken(String idToken) {
        try {
            FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(idToken, true);
            return new TokenVerification(token.getUid(), token.getEmail(), token.getName());
        } catch (FirebaseAuthException e) {
            throw new InvalidTokenException("Invalid Google token", e);
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
            throw new FirebaseUserDeletionException("Failed to delete Firebase user during rollback: " + e.getMessage(), e);
        }
    }
}
