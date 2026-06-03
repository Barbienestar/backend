package com.itesm.interfaces.rest;

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
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestQuery;

@Tag(name = "Geography")
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
    @Operation(
            summary = "List suburbs by city",
            description = "Returns all suburbs (colonias) belonging to the given city. No authentication required.")
    @Parameter(name = "id_city", description = "City identifier", required = true)
    @APIResponse(
            responseCode = "200",
            description = "List of suburbs for the requested city",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = SuburbDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value = "[{\"id\": 100, \"name\": \"Centro\", \"zip_code\": \"44100\"},"
                                                    + " {\"id\": 101, \"name\": \"Chapalita\", \"zip_code\":"
                                                    + " \"44500\"}]")))
    public Response getSuburbsByState(@RestQuery("id_city") Integer idCity) {
        List<SuburbDto> suburbs = getSuburbsByCityUseCase.execute(idCity);
        return Response.ok(suburbs).build();
    }
}
