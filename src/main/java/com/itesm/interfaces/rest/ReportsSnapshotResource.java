package com.itesm.interfaces.rest;

import com.itesm.application.dto.PeriodReportsByHospitalResponse;
import com.itesm.application.dto.PeriodReportsByHospitalWithStockResponse;
import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.GetPeriodReportsByHospitalUseCase;
import com.itesm.application.usecase.GetPeriodReportsByHospitalWithStockUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Reports", description = "Citizen reports and report status management")
@Path("/reports-snapshots")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportsSnapshotResource {
    private final GetPeriodReportsByHospitalUseCase getPeriodReportsByHospitalUseCase;
    private final GetPeriodReportsByHospitalWithStockUseCase getPeriodReportsByHospitalWithStockUseCase;

    @Inject
    public ReportsSnapshotResource(
            GetPeriodReportsByHospitalUseCase getPeriodReportsByHospitalUseCase,
            GetPeriodReportsByHospitalWithStockUseCase getPeriodReportsByHospitalWithStockUseCase) {
        this.getPeriodReportsByHospitalUseCase = getPeriodReportsByHospitalUseCase;
        this.getPeriodReportsByHospitalWithStockUseCase = getPeriodReportsByHospitalWithStockUseCase;
    }

    @Path("/period/{hospital-id}")
    @GET
    @RequireRoles({"health"})
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Get period reports by hospital",
            description =
                    "Returns a list of report counts grouped by date for a given hospital within the specified date range. Requires health role.")
    @Parameter(name = "hospital-id", description = "Hospital identifier", required = true)
    @Parameter(name = "start_date", description = "Start date (inclusive), format yyyy-MM-dd", required = true)
    @Parameter(name = "end_date", description = "End date (inclusive), format yyyy-MM-dd", required = true)
    @APIResponse(
            responseCode = "200",
            description = "List of daily report counts for the hospital in the period",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value =
                                                    "[{\"report_date\": \"2024-06-01\", \"total_accepted_reports\": 5}, {\"report_date\": \"2024-06-02\", \"total_accepted_reports\": 3}]")))
    @APIResponse(
            responseCode = "400",
            description = "hospital-id, start_date, or end_date is missing",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples = @ExampleObject(value = "{\"error\": \"hospital-id, start_date, and end_date are required\"}")))
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the health role")
    public Response getPeriodReports(
            @PathParam("hospital-id") Integer idHospital,
            @QueryParam("start_date") LocalDate startDate,
            @QueryParam("end_date") LocalDate endDate) {
        if (idHospital == null || startDate == null || endDate == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"hospital-id, start_date, and end_date are required\"}")
                    .build();
        }
        List<PeriodReportsByHospitalResponse> result =
                getPeriodReportsByHospitalUseCase.execute(idHospital, startDate, endDate);
        return Response.ok(result).build();
    }

    @Path("/period/{hospital-id}/with-stock")
    @GET
    @RequireRoles({"health"})
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Get period reports by hospital with stock data",
            description =
                    "Returns a list of report counts and stock levels grouped by date for a given hospital within the specified date range. Requires health role.")
    @Parameter(name = "hospital-id", description = "Hospital identifier", required = true)
    @Parameter(name = "start_date", description = "Start date (inclusive), format yyyy-MM-dd", required = true)
    @Parameter(name = "end_date", description = "End date (inclusive), format yyyy-MM-dd", required = true)
    @APIResponse(
            responseCode = "200",
            description = "List of daily report counts with stock data for the hospital in the period",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value =
                                                    "[{\"report_date\": \"2024-06-01\", \"total_accepted_reports\": 5, \"total_stock\": 120}, {\"report_date\": \"2024-06-02\", \"total_accepted_reports\": 3, \"total_stock\": 115}]")))
    @APIResponse(
            responseCode = "400",
            description = "hospital-id, start-date, or end-date is missing",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples = @ExampleObject(value = "{\"error\": \"hospital-id, start-date, and end-date are required\"}")))
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the health role")
    public Response getPeriodReportsWithStock(
            @PathParam("hospital-id") Integer idHospital,
            @QueryParam("start_date") LocalDate startDate,
            @QueryParam("end_date") LocalDate endDate) {
        if (idHospital == null || startDate == null || endDate == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"hospital-id, start-date, and end-date are required\"}")
                    .build();
        }
        List<PeriodReportsByHospitalWithStockResponse> result =
                getPeriodReportsByHospitalWithStockUseCase.execute(idHospital, startDate, endDate);
        return Response.ok(result).build();
    }
}
