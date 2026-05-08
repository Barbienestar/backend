package com.itesm.infrastructure.security;

import com.itesm.application.security.AuthenticatedUserContext;
import com.itesm.application.security.CurrentUser;
import com.itesm.domain.models.User;

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

        User user = new User();
        user.setId(1L);
        user.setName("John");
        user.setLastName1("Pork");
        user.setEmail("john@itesm.com");
        user.setProviderUuid("1234567890");
        CurrentUser currentUser = new CurrentUser(user);
        authUserContext.setCurrentUser(currentUser);
    }
}
