package com.itesm.infrastructure.mapper.exceptions;

import com.itesm.domain.exceptions.SignedUrlGenerationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class SignedUrlGenerationExceptionMapper implements ExceptionMapper<SignedUrlGenerationException> {
    @Override
    public Response toResponse(SignedUrlGenerationException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", "SIGNED_URL_GENERATION_FAILED", "message", e.getMessage()))
                .build();
    }
}
