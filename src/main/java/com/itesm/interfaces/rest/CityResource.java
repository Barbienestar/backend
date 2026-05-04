package com.itesm.interfaces.rest;

import java.util.List;

import com.itesm.application.dto.CityDto;
import com.itesm.application.dto.GetCitiesByStateDto;
import com.itesm.application.usecase.GetCitiesByStateUseCase;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/cities")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CityResource {
    private final GetCitiesByStateUseCase getCitiesByStateUseCase;
    
    @Inject CityResource(GetCitiesByStateUseCase getCitiesByStateUseCase) {
        this.getCitiesByStateUseCase = getCitiesByStateUseCase;
    }

    @GET
    public Response getCitiesByState(GetCitiesByStateDto getCitiesByStateDto) {
        List<CityDto> cities = getCitiesByStateUseCase.execute(getCitiesByStateDto);
        return Response.ok(cities).build();
    }
}
