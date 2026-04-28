package com.itesm.interfaces.rest;

import java.util.List;

import com.itesm.application.usecase.GetAllStatesUseCase;
import com.itesm.application.dto.StateDto;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/states")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StateResource {
    private final GetAllStatesUseCase getAllStatesUseCase;

    @Inject
    public StateResource(GetAllStatesUseCase getAllStatesUseCase) {
        this.getAllStatesUseCase = getAllStatesUseCase;
    }

    @GET
    public Response getAll() {
        List<StateDto> states = getAllStatesUseCase.execute();
        return Response.ok(states).build();
    }
}
