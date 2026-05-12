package com.itesm.application.security;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class RoleAuthorizationFilter implements ContainerRequestFilter {
    @Inject AuthenticatedUserContext authContext;

    @Inject ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        if (method == null) {
            return;
        }

        RequireRoles requireRoles = method.getAnnotation(RequireRoles.class);
        if (requireRoles == null) {
            return;
        }

        CurrentUser currentUser = authContext.getCurrentUser();
        if (currentUser == null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String[] allowedRoles = requireRoles.value();
        if (Arrays.stream(allowedRoles).anyMatch(currentUser::hasRole)) {
            return;
        }
        requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
    }
}
