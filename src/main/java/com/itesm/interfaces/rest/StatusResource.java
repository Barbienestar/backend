package com.itesm.interfaces.rest;

import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.ListStatusUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Reports", description = "Citizen reports and report status management")
@Path("/status")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StatusResource {

    private final ListStatusUseCase listStatusUseCase;

    @Inject
    public StatusResource(ListStatusUseCase listStatusUseCase) {
        this.listStatusUseCase = listStatusUseCase;
    }

    @GET
    @RequireRoles({"admin"})
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "List all report statuses",
            description = "Returns all available report statuses. Requires admin role.")
    @APIResponse(
            responseCode = "200",
            description = "List of statuses",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value =
                                                    "[{\"id\": 1, \"name\": \"Pendiente\"}, {\"id\": 2, \"name\": \"En proceso\"}, {\"id\": 3, \"name\": \"Resuelto\"}]")))
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the admin role")
    public Response getStatuses() {
        return Response.ok(listStatusUseCase.execute()).build();
    }
}
