package com.itesm.interfaces.rest;

import com.itesm.application.dto.StateDto;
import com.itesm.application.dto.StateSupplyDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.GetAllStatesUseCase;
import com.itesm.application.usecase.GetStateSupplyHeatmapUseCase;
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
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Geography", description = "Geographic reference data: states, cities and suburbs")
@Path("/states")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StateResource {

    private final GetAllStatesUseCase getAllStatesUseCase;
    private final GetStateSupplyHeatmapUseCase getStateSupplyHeatmapUseCase;

    @Inject
    public StateResource(
            GetAllStatesUseCase getAllStatesUseCase, GetStateSupplyHeatmapUseCase getStateSupplyHeatmapUseCase) {
        this.getAllStatesUseCase = getAllStatesUseCase;
        this.getStateSupplyHeatmapUseCase = getStateSupplyHeatmapUseCase;
    }

    @GET
    @PermitPublic
    @Operation(summary = "List all states", description = "Returns all Mexican states. No authentication required.")
    @APIResponse(
            responseCode = "200",
            description = "List of states",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = StateDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value =
                                                    "[{\"id\": 1, \"name\": \"Jalisco\"}, {\"id\": 2, \"name\": \"Nuevo León\"}]")))
    public Response getAll() {
        List<StateDto> states = getAllStatesUseCase.execute();
        return Response.ok(states).build();
    }

    @GET
    @Path("/heatmap")
    @RequireRoles({"health"})
    @Operation(
            summary = "State supply heatmap",
            description = "Returns average stock level per state for choropleth map. Requires health role.")
    @APIResponse(
            responseCode = "200",
            description = "List of states with their supply level",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = StateSupplyDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value =
                                                    "[{\"stateId\": 1, \"stateName\": \"Jalisco\", \"avgStock\": 82.5, \"level\": \"CA-01\"}]")))
    public Response getHeatmap() {
        List<StateSupplyDto> data = getStateSupplyHeatmapUseCase.execute();
        return Response.ok(data).build();
    }
}
