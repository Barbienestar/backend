package com.itesm.infrastructure.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;

import io.quarkus.arc.profile.UnlessBuildProfile;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Provider
@Priority(Priorities.AUTHENTICATION)
@UnlessBuildProfile("test")
public class FirebaseAuthFilter implements ContainerRequestFilter {
    private final UserRepository userRepository;
    private final AuthenticatedUserContext authUserContext;

    @Inject
    public FirebaseAuthFilter(
            UserRepository userRepository, AuthenticatedUserContext authUserContext) {
        this.userRepository = userRepository;
        this.authUserContext = authUserContext;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        // NOTE: Aqui podemos poner un .contains("public")
        if (path.equals("/user")
                || path.equals("/health")
                || path.startsWith("/medicines")
                || path.startsWith("/hospitals")
                || path.startsWith("/image")
                || path.startsWith("/states")) {
            return;
        }

        String authHeader = requestContext.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity(Map.of("message", "Token not found"))
                            .type(MediaType.APPLICATION_JSON)
                            .build());
            return;
        }

        try {
            String idToken = authHeader.replace("Bearer ", "");
            FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(idToken, true);
            Optional<User> userOptional = userRepository.findByProviderUuid(token.getUid());
            if (userOptional.isEmpty()) {
                requestContext.abortWith(
                        Response.status(Response.Status.UNAUTHORIZED)
                                .entity(Map.of("message", "User not found"))
                                .type(MediaType.APPLICATION_JSON)
                                .build());
                return;
            }
            User user = userOptional.get();
            CurrentUser currentUser = new CurrentUser(user);
            authUserContext.setCurrentUser(currentUser);
        } catch (FirebaseAuthException e) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity(Map.of("message", "Invalid token"))
                            .type(MediaType.APPLICATION_JSON)
                            .build());
        }
    }
}
