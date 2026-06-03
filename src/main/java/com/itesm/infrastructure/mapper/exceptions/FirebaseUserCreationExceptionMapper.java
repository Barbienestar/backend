package com.itesm.infrastructure.mapper.exceptions;

import com.itesm.domain.exceptions.FirebaseUserCreationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class FirebaseUserCreationExceptionMapper implements ExceptionMapper<FirebaseUserCreationException> {
    @Override
    public Response toResponse(FirebaseUserCreationException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", "FIREBASE_USER_CREATION_FAILED", "message", e.getMessage()))
                .build();
    }
}
