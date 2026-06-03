package com.itesm.infrastructure.mapper.exceptions;

import com.itesm.domain.exceptions.EncryptionException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class EncryptionExceptionMapper implements ExceptionMapper<EncryptionException> {
    @Override
    public Response toResponse(EncryptionException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", "ENCRYPTION_FAILED", "message", e.getMessage()))
                .build();
    }
}
