package com.itesm.interfaces.rest;

import java.util.List;

import org.jboss.resteasy.reactive.RestQuery;

import com.itesm.application.dto.SuburbDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.usecase.GetSuburbsByCityUseCase;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/suburbs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SuburbResource {
    private final GetSuburbsByCityUseCase getSuburbsByCityUseCase;

    @Inject
    SuburbResource(GetSuburbsByCityUseCase getSuburbsByCityUseCase) {
        this.getSuburbsByCityUseCase = getSuburbsByCityUseCase;
    }

    @GET
    @PermitPublic
    public Response getSuburbsByState(@RestQuery Integer id_city) {
        List<SuburbDto> suburbs = getSuburbsByCityUseCase.execute(id_city);
        return Response.ok(suburbs).build();
    }
}
