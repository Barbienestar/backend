package com.itesm.infrastructure.mapper.exceptions;

import com.itesm.domain.exceptions.EmailAlreadyExistsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class EmailAlreadyExistsExceptionMapper implements ExceptionMapper<EmailAlreadyExistsException> {
    @Override
    public Response toResponse(EmailAlreadyExistsException e) {
        return Response.status(Response.Status.CONFLICT)
                .entity(Map.of("error", "EMAIL_ALREADY_EXISTS", "message", e.getMessage()))
                .build();
    }
}
