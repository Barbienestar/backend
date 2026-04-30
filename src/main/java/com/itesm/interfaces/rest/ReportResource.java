package com.itesm.interfaces.rest;

import com.itesm.application.dto.CreateReportDto;
import com.itesm.application.dto.ReportDto;
import com.itesm.application.usecase.CreateReportUseCase;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportResource {

    private final CreateReportUseCase createReportUseCase;

    @Inject
    public ReportResource(CreateReportUseCase createReportUseCase) {
        this.createReportUseCase = createReportUseCase;
    }

    @POST
    public Response createReport(@Valid CreateReportDto dto) {
        ReportDto report = createReportUseCase.execute(dto);
        return Response.status(Response.Status.CREATED).entity(report).build();
    }
}

