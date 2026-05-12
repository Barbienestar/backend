package com.itesm.interfaces.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.Map;

/** HealthResource */
@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {
    @GET
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
