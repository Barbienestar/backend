package com.itesm.interfaces.rest;

import com.itesm.application.dto.HospitalDto;
import com.itesm.application.dto.PagedHospitalCriticalMedicinesDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.GetCriticalMedicinesUseCase;
import com.itesm.application.usecase.GetHospitalsUseCase;
import com.itesm.application.usecase.GetMyHospitalsUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Hospitals", description = "Hospital catalog and user-hospital assignment")
@Path("/hospitals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HospitalResource {

    private final GetHospitalsUseCase getHospitalsUseCase;
    private final GetMyHospitalsUseCase getMyHospitalsUseCase;
    private final GetCriticalMedicinesUseCase getCriticalMedicinesUseCase;

    @Inject
    public HospitalResource(
            GetHospitalsUseCase getHospitalsUseCase,
            GetMyHospitalsUseCase getMyHospitalsUseCase,
            GetCriticalMedicinesUseCase getCriticalMedicinesUseCase) {
        this.getHospitalsUseCase = getHospitalsUseCase;
        this.getMyHospitalsUseCase = getMyHospitalsUseCase;
        this.getCriticalMedicinesUseCase = getCriticalMedicinesUseCase;
    }

    @GET
    @PermitPublic
    @Operation(
            summary = "List all hospitals",
            description = "Returns all hospitals registered in the platform. No authentication required.")
    @APIResponse(
            responseCode = "200",
            description = "List of hospitals",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = HospitalDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value =
                                                    "[{\"id\": 1, \"name\": \"Hospital Civil de Guadalajara\"}, {\"id\": 2, \"name\": \"IMSS Belén\"}]")))
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
            description = "Returns only the hospitals associated with the authenticated user. Requires health role.")
    @APIResponse(
            responseCode = "200",
            description = "List of hospitals assigned to the current user",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = HospitalDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value = "[{\"id\": 3, \"name\": \"Hospital General de Zona\"}]")))
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the health role")
    public Response getMyHospitals() {
        List<HospitalDto> hospitals = getMyHospitalsUseCase.execute();
        return Response.ok(hospitals).build();
    }

    @GET
    @Path("{idHospital}/critical-medicines")
    @RequireRoles({"health"})
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "List hospitals with critical medicine stock",
            description =
                    "Returns the hospitals assigned to the authenticated user that have at least one medicine with stock ≤ 9. "
                            + "Results within each hospital are ordered by stock ASC. Hospitals with no critical medicines are excluded. Requires health role.")
    @APIResponse(
            responseCode = "200",
            description = "Paged list of critical medicines for the hospital",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PagedHospitalCriticalMedicinesDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value =
                                                    "{\"hospital_id\": 1, \"hospital_name\": \"Hospital General\", \"critical_medicines\": [{\"id\": 1, \"generic_name\": \"Metformina\", \"stock\": 80}], \"total_elements\": 35, \"total_pages\": 4, \"current_page\": 0}")))
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the health role")
    public Response getCriticalMedicines(
            @PathParam("idHospital") Integer idHospital,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {
        PagedHospitalCriticalMedicinesDto result = getCriticalMedicinesUseCase.execute(idHospital, page, size);
        return Response.ok(result).build();
    }
}
