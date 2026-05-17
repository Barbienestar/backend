package com.itesm.infrastructure.security;

import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.application.security.PermitPublic;
import com.itesm.domain.models.User;
import com.itesm.domain.repository.UserRepository;

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
public class MockFirebaseAuthFilter implements ContainerRequestFilter {
    private final UserRepository userRepository;
    private final AuthenticatedUserContext authUserContext;
    private final ResourceInfo resourceInfo;

    @Inject
    public MockFirebaseAuthFilter(
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
        boolean isPublic =
                method != null && method.isAnnotationPresent(PermitPublic.class)
                        || (resourceClass != null
                                && resourceClass.isAnnotationPresent(PermitPublic.class));

        if (isPublic) {
            tryOptionalAuth(requestContext);
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

        String idToken = authHeader.replace("Bearer ", "");
        Optional<User> userOptional = userRepository.findByProviderUuid(idToken);
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
    }

    private void tryOptionalAuth(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String idToken = authHeader.replace("Bearer ", "");
        Optional<User> userOptional = userRepository.findByProviderUuid(idToken);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            CurrentUser currentUser = new CurrentUser(user);
            authUserContext.setCurrentUser(currentUser);
        }
    }
}
