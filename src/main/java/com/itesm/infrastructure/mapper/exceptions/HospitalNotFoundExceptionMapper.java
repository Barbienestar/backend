package com.itesm.infrastructure.mapper.exceptions;

import com.itesm.domain.exceptions.HospitalNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class HospitalNotFoundExceptionMapper implements ExceptionMapper<HospitalNotFoundException> {
    @Override
    public Response toResponse(HospitalNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "HOSPITAL_NOT_FOUND", "message", e.getMessage()))
                .build();
    }
}
