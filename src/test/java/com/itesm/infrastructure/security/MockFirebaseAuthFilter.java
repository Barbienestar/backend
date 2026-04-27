package com.itesm.infrastructure.security;

import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

/** MockFirebaseAuthFilter */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class MockFirebaseAuthFilter implements ContainerRequestFilter {
    @Inject AuthenticatedUserContext authUserContext;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        if (path.equals("/user")) {
            return;
        }

        String authHeader = requestContext.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(401).build());
            return;
        }

        CurrentUser currentUser =
                new CurrentUser(1, "firebaseUid", "email@example.com", Byte.parseByte("1"), "John");
        authUserContext.setCurrentUser(currentUser);
    }
}
