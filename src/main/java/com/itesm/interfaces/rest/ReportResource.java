package com.itesm.interfaces.rest;

import com.itesm.application.dto.CreateReportDto;
import com.itesm.application.dto.ReportDto;
import com.itesm.application.dto.ReportSummaryDto;
import com.itesm.application.usecase.CreateReportUseCase;
import com.itesm.application.usecase.GetMyReportsUseCase;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportResource {

    private final CreateReportUseCase createReportUseCase;
    private final GetMyReportsUseCase getMyReportsUseCase;

    @Inject
    public ReportResource(CreateReportUseCase createReportUseCase, GetMyReportsUseCase getMyReportsUseCase) {
        this.createReportUseCase = createReportUseCase;
        this.getMyReportsUseCase = getMyReportsUseCase;
    }

    @POST
    public Response createReport(@Valid CreateReportDto dto) {
        ReportDto report = createReportUseCase.execute(dto);
        return Response.status(Response.Status.CREATED).entity(report).build();
    }

    @GET
    @Path("/me")
    public Response getMyReports() {
        List<ReportSummaryDto> reports = getMyReportsUseCase.execute();
        return Response.ok(reports).build();
    }
}

