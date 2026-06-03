package com.itesm.infrastructure.mapper.exceptions;

import com.itesm.domain.exceptions.ImageNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class ImageNotFoundExceptionMapper implements ExceptionMapper<ImageNotFoundException> {
    @Override
    public Response toResponse(ImageNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "IMAGE_NOT_FOUND", "message", e.getMessage()))
                .build();
    }
}
