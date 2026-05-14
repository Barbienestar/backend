package com.itesm.interfaces.rest;

import com.itesm.application.dto.CreateReportDto;
import com.itesm.application.dto.FullReportResponse;
import com.itesm.application.dto.PagedResult;
import com.itesm.application.dto.ReportDto;
import com.itesm.application.dto.ReportSummaryDto;
import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.CreateReportUseCase;
import com.itesm.application.usecase.GetMyReportsUseCase;
import com.itesm.application.usecase.GetReportCountByStatusUseCase;
import com.itesm.application.usecase.GetReportsByStatusUseCase;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportResource {

    private final CreateReportUseCase createReportUseCase;
    private final GetMyReportsUseCase getMyReportsUseCase;
    private final GetReportsByStatusUseCase getReportsByStatusUseCase;
    private final GetReportCountByStatusUseCase getReportCountByStatusUseCase;

    @Inject
    public ReportResource(
            CreateReportUseCase createReportUseCase,
            GetMyReportsUseCase getMyReportsUseCase,
            GetReportsByStatusUseCase getReportsByStatusUseCase,
            GetReportCountByStatusUseCase getReportCountByStatusUseCase) {
        this.createReportUseCase = createReportUseCase;
        this.getMyReportsUseCase = getMyReportsUseCase;
        this.getReportsByStatusUseCase = getReportsByStatusUseCase;
        this.getReportCountByStatusUseCase = getReportCountByStatusUseCase;
    }

    @POST
    @RequireRoles({"citizen"})
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

    @GET
    @Path("/status/{statusId}")
    @RequireRoles({"admin"})
    public Response getReportsByStatus(
            @PathParam("statusId") Integer statusId,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {
        try {
            PagedResult<FullReportResponse> result =
                    getReportsByStatusUseCase.execute(statusId, page, size);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/status/{statusId}/count")
    @RequireRoles({"admin"})
    public Response getReportsByStatusCount(@PathParam("statusId") Integer statusId) {
        try {
            long count = getReportCountByStatusUseCase.execute(statusId);
            return Response.ok(Map.of("count", count)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
