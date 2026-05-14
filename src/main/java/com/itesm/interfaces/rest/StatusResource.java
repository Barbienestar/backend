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

/** StatusResource */
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
    public Response getStatuses() {
        return Response.ok(listStatusUseCase.execute()).build();
    }
}
