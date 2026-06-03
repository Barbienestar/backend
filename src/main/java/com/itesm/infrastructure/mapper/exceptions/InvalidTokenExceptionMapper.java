package com.itesm.infrastructure.mapper.exceptions;

import com.itesm.domain.exceptions.InvalidTokenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class InvalidTokenExceptionMapper implements ExceptionMapper<InvalidTokenException> {
    @Override
    public Response toResponse(InvalidTokenException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Map.of("error", "INVALID_TOKEN", "message", e.getMessage()))
                .build();
    }
}
