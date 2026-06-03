package com.itesm.infrastructure.mapper.exceptions;

import com.itesm.domain.exceptions.ImageUploadException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class ImageUploadExceptionMapper implements ExceptionMapper<ImageUploadException> {
    @Override
    public Response toResponse(ImageUploadException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", "IMAGE_UPLOAD_FAILED", "message", e.getMessage()))
                .build();
    }
}
