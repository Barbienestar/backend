package com.itesm.interfaces.rest;

import com.itesm.application.dto.CreateReportDto;
import com.itesm.application.dto.FullReportResponse;
import com.itesm.application.dto.PagedResult;
import com.itesm.application.dto.ReportDto;
import com.itesm.application.dto.ReportSummaryDto;
import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.ChangeReportStatusUseCase;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Reports", description = "Citizen reports and report status management")
@Path("/reports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportResource {

    private final CreateReportUseCase createReportUseCase;
    private final GetMyReportsUseCase getMyReportsUseCase;
    private final GetReportsByStatusUseCase getReportsByStatusUseCase;
    private final GetReportCountByStatusUseCase getReportCountByStatusUseCase;
    private final ChangeReportStatusUseCase changeReportStatusUseCase;

    @Inject
    public ReportResource(
            CreateReportUseCase createReportUseCase,
            GetMyReportsUseCase getMyReportsUseCase,
            GetReportsByStatusUseCase getReportsByStatusUseCase,
            GetReportCountByStatusUseCase getReportCountByStatusUseCase,
            ChangeReportStatusUseCase changeReportStatusUseCase) {
        this.createReportUseCase = createReportUseCase;
        this.getMyReportsUseCase = getMyReportsUseCase;
        this.getReportsByStatusUseCase = getReportsByStatusUseCase;
        this.getReportCountByStatusUseCase = getReportCountByStatusUseCase;
        this.changeReportStatusUseCase = changeReportStatusUseCase;
    }

    @POST
    @RequireRoles({"citizen"})
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Create a report",
            description = "Submits a new medicine shortage report. Requires citizen role.")
    @RequestBody(
            description = "Report data including medicine, hospital, description and image URL",
            required = true,
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CreateReportDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value =
                                                    "{\"medicine_id\": 1, \"hospital_id\": 2, \"description\": \"No hay stock desde hace 3 días.\", \"image_url\": \"https://storage.example.com/reports/abc.jpg\"}")))
    @APIResponse(
            responseCode = "201",
            description = "Report created successfully",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ReportDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "created",
                                            value =
                                                    "{\"id\": 10, \"medicine_name\": \"Paracetamol\", \"hospital_name\": \"Hospital Civil\", \"status_id\": 1, \"description\": \"No hay stock desde hace 3 días.\", \"created_at\": \"2024-06-01T10:00:00\"}")))
    @APIResponse(responseCode = "400", description = "Validation error in request body")
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the citizen role")
    public Response createReport(@Valid CreateReportDto dto) {
        ReportDto report = createReportUseCase.execute(dto);
        return Response.status(Response.Status.CREATED).entity(report).build();
    }

    @GET
    @Path("/me")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "List my reports",
            description =
                    "Returns all reports submitted by the authenticated user. Any authenticated role is accepted.")
    @APIResponse(
            responseCode = "200",
            description = "List of the user's reports",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = ReportSummaryDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value =
                                                    "[{\"id\": 10, \"medicine_name\": \"Paracetamol\", \"hospital_name\": \"Hospital Civil\", \"status\": \"Pendiente\", \"description\": \"Sin stock\", \"image_url\": \"https://...\", \"created_at\": \"2024-06-01T10:00:00\", \"updated_at\": \"2024-06-01T10:00:00\"}]")))
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    public Response getMyReports() {
        List<ReportSummaryDto> reports = getMyReportsUseCase.execute();
        return Response.ok(reports).build();
    }

    @GET
    @Path("/status/{statusId}")
    @RequireRoles({"admin"})
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "List reports by status",
            description = "Returns a paginated list of reports filtered by status. Requires admin role.")
    @Parameter(name = "statusId", description = "Status identifier to filter by", required = true)
    @Parameter(name = "page", description = "Page number (0-based, default 0)")
    @Parameter(name = "size", description = "Page size (default 10)")
    @APIResponse(
            responseCode = "200",
            description = "Paginated list of reports",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = PagedResult.class),
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value =
                                                    "{\"items\": [{\"id\": 10, \"description\": \"Sin stock\", \"image_url\": \"https://...\", \"user_full_name\": \"Ana García\", \"medicine_name\": \"Paracetamol\", \"medicine_presentation\": \"20 tablets\", \"medicine_dosage_form\": \"Tablet\", \"hospital_name\": \"Hospital Civil\", \"created_at\": \"2024-06-01T10:00:00\"}], \"page\": 0, \"page_size\": 10, \"total_items\": 1, \"total_pages\": 1}")))
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the admin role")
    @APIResponse(responseCode = "500", description = "Unexpected server error")
    public Response getReportsByStatus(
            @PathParam("statusId") Integer statusId,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) {
        try {
            PagedResult<FullReportResponse> result = getReportsByStatusUseCase.execute(statusId, page, size);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{reportId}/status/{statusId}")
    @RequireRoles({"admin"})
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Change report status", description = "Updates the status of a report. Requires admin role.")
    @Parameter(name = "reportId", description = "Report identifier", required = true)
    @Parameter(name = "statusId", description = "New status identifier", required = true)
    @APIResponse(responseCode = "200", description = "Status updated successfully")
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the admin role")
    public Response changeReportStatus(
            @PathParam("reportId") Integer reportId, @PathParam("statusId") Integer statusId) {
        changeReportStatusUseCase.execute(reportId, statusId);
        return Response.ok().build();
    }

    @GET
    @Path("/status/{statusId}/count")
    @RequireRoles({"admin"})
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Count reports by status",
            description = "Returns the total number of reports for the given status. Requires admin role.")
    @Parameter(name = "statusId", description = "Status identifier to count", required = true)
    @APIResponse(
            responseCode = "200",
            description = "Report count",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples = @ExampleObject(name = "sample", value = "{\"count\": 42}")))
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the admin role")
    @APIResponse(responseCode = "500", description = "Unexpected server error")
    public Response getReportsByStatusCount(@PathParam("statusId") Integer statusId) {
        try {
            long count = getReportCountByStatusUseCase.execute(statusId);
            return Response.ok(Map.of("count", count)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
