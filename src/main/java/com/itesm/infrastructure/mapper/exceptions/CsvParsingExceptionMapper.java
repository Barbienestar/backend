package com.itesm.infrastructure.mapper.exceptions;

import com.itesm.domain.exceptions.CsvParsingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class CsvParsingExceptionMapper implements ExceptionMapper<CsvParsingException> {
    @Override
    public Response toResponse(CsvParsingException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", "CSV_PARSING_ERROR", "message", e.getMessage()))
                .build();
    }
}
