package com.itesm.infrastructure.mapper.exceptions;

import com.itesm.domain.exceptions.InvalidRoleException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class InvalidRoleExceptionMapper implements ExceptionMapper<InvalidRoleException> {
    @Override
    public Response toResponse(InvalidRoleException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "INVALID_ROLE", "message", e.getMessage()))
                .build();
    }
}
