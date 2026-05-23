package com.itesm.interfaces.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.Instant;
import java.util.Map;

@Tag(name = "Health", description = "Application health check")
@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    @GET
    @Operation(summary = "Health check", description = "Returns the current status and timestamp of the application.")
    @APIResponse(
        responseCode = "200",
        description = "Application is up",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            examples = @ExampleObject(
                name = "up",
                value = "{\"status\": \"UP\", \"name\": \"version\", \"timestamp\": \"2024-01-01T00:00:00Z\"}"
            )
        )
    )
    public Response status() {
        return Response.ok(
                        Map.of(
                                "status",
                                "UP",
                                "name",
                                "version",
                                "timestamp",
                                Instant.now().toString()))
                .build();
    }
}
