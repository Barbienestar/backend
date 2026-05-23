package com.itesm.interfaces.rest;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestQuery;

import com.itesm.application.dto.CityDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.usecase.GetCitiesByStateUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import org.jboss.resteasy.reactive.RestQuery;

@Tag(name = "Geography")
@Path("/cities")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CityResource {

    private final GetCitiesByStateUseCase getCitiesByStateUseCase;

    @Inject
    CityResource(GetCitiesByStateUseCase getCitiesByStateUseCase) {
        this.getCitiesByStateUseCase = getCitiesByStateUseCase;
    }

    @GET
    @PermitPublic
    @Operation(
        summary = "List cities by state",
        description = "Returns all cities belonging to the given state. No authentication required."
    )
    @Parameter(name = "id_state", description = "State identifier", required = true)
    @APIResponse(
        responseCode = "200",
        description = "List of cities for the requested state",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = CityDto.class),
            examples = @ExampleObject(
                name = "sample",
                value = "[{\"id\": 10, \"name\": \"Guadalajara\"}, {\"id\": 11, \"name\": \"Zapopan\"}]"
            )
        )
    )
    public Response getCitiesByState(@RestQuery Byte id_state) {
        List<CityDto> cities = getCitiesByStateUseCase.execute(id_state);
        return Response.ok(cities).build();
    }
}
