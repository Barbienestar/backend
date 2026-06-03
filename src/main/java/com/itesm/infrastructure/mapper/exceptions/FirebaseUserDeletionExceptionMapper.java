package com.itesm.infrastructure.mapper.exceptions;

import com.itesm.domain.exceptions.FirebaseUserDeletionException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class FirebaseUserDeletionExceptionMapper implements ExceptionMapper<FirebaseUserDeletionException> {
    @Override
    public Response toResponse(FirebaseUserDeletionException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", "FIREBASE_USER_DELETION_FAILED", "message", e.getMessage()))
                .build();
    }
}
