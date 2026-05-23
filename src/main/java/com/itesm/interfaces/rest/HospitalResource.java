package com.itesm.interfaces.rest;

import com.itesm.application.dto.HospitalDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.GetHospitalsUseCase;
import com.itesm.application.usecase.GetMyHospitalsUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Hospitals", description = "Hospital catalog and user-hospital assignment")
@Path("/hospitals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HospitalResource {

    private final GetHospitalsUseCase getHospitalsUseCase;
    private final GetMyHospitalsUseCase getMyHospitalsUseCase;

    @Inject
    public HospitalResource(GetHospitalsUseCase getHospitalsUseCase, GetMyHospitalsUseCase getMyHospitalsUseCase) {
        this.getHospitalsUseCase = getHospitalsUseCase;
        this.getMyHospitalsUseCase = getMyHospitalsUseCase;
    }

    @GET
    @PermitPublic
    @Operation(
        summary = "List all hospitals",
        description = "Returns all hospitals registered in the platform. No authentication required."
    )
    @APIResponse(
        responseCode = "200",
        description = "List of hospitals",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = HospitalDto.class),
            examples = @ExampleObject(
                name = "sample",
                value = "[{\"id\": 1, \"name\": \"Hospital Civil de Guadalajara\"}, {\"id\": 2, \"name\": \"IMSS Belén\"}]"
            )
        )
    )
    public Response getAll() {
        List<HospitalDto> hospitals = getHospitalsUseCase.execute();
        return Response.ok(hospitals).build();
    }

    @GET
    @Path("/my-hospitals")
    @RequireRoles({"health"})
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
        summary = "List hospitals assigned to the current user",
        description = "Returns only the hospitals associated with the authenticated user. Requires health role."
    )
    @APIResponse(
        responseCode = "200",
        description = "List of hospitals assigned to the current user",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = HospitalDto.class),
            examples = @ExampleObject(
                name = "sample",
                value = "[{\"id\": 3, \"name\": \"Hospital General de Zona\"}]"
            )
        )
    )
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the health role")
    public Response getMyHospitals() {
        List<HospitalDto> hospitals = getMyHospitalsUseCase.execute();
        return Response.ok(hospitals).build();
    }
}
