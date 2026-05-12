package com.itesm.infrastructure.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.application.security.PermitPublic;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;

import io.quarkus.arc.profile.UnlessBuildProfile;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

@Provider
@Priority(Priorities.AUTHENTICATION)
@UnlessBuildProfile("test")
public class FirebaseAuthFilter implements ContainerRequestFilter {
    private final UserRepository userRepository;
    private final AuthenticatedUserContext authUserContext;
    private final ResourceInfo resourceInfo;

    @Inject
    public FirebaseAuthFilter(
            UserRepository userRepository,
            AuthenticatedUserContext authUserContext,
            ResourceInfo resourceInfo) {
        this.userRepository = userRepository;
        this.authUserContext = authUserContext;
        this.resourceInfo = resourceInfo;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        Class<?> resourceClass = resourceInfo.getResourceClass();
        if (method != null && method.isAnnotationPresent(PermitPublic.class)
                || (resourceClass != null
                        && resourceClass.isAnnotationPresent(PermitPublic.class))) {
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
