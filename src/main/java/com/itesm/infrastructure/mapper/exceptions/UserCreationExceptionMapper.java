package com.itesm.infrastructure.mapper.exceptions;

import com.itesm.domain.exceptions.UserCreationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class UserCreationExceptionMapper implements ExceptionMapper<UserCreationException> {
    @Override
    public Response toResponse(UserCreationException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", "USER_CREATION_FAILED", "message", e.getMessage()))
                .build();
    }
}
