package com.itesm.infrastructure.mapper.exceptions;

import com.itesm.domain.exceptions.HospitalNotAssignedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class HospitalNotAssignedExceptionMapper implements ExceptionMapper<HospitalNotAssignedException> {
    @Override
    public Response toResponse(HospitalNotAssignedException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(Map.of("error", "HOSPITAL_NOT_ASSIGNED", "message", e.getMessage()))
                .build();
    }
}
