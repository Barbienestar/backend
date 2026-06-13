package com.itesm.interfaces.rest;

import com.itesm.application.dto.MedicinesHospitalsStockDto;
import com.itesm.application.dto.MonthlyReportsDto;
import com.itesm.application.dto.MonthlyReportsResponse;
import com.itesm.application.dto.StockAveragesDto;
import com.itesm.application.dto.StockAveragesResponse;
import com.itesm.application.dto.StockReportDto;
import com.itesm.application.dto.StockReportResponse;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.GetMonthlyReportsUseCase;
import com.itesm.application.usecase.GetStockAveragesByHospitalUseCase;
import com.itesm.application.usecase.GetStockByMedicineUseCase;
import com.itesm.application.usecase.GetStockReportByHospitalUseCase;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Medicines", description = "Medicine catalog and hospital stock management")
@Path("/medicines-hospitals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicinesHospitalsResource {

    private static final String ERROR_ID_HOSPITAL_REQUIRED = "{\"error\": \"idHospital is required\"}";

    private final GetStockByMedicineUseCase getStockByMedicineUseCase;
    private final GetStockAveragesByHospitalUseCase getStockAveragesByHospitalUseCase;
    private final GetStockReportByHospitalUseCase getStockReportByHospitalUseCase;
    private final GetMonthlyReportsUseCase getMonthlyReportsUseCase;

    @Inject
    public MedicinesHospitalsResource(
            GetStockByMedicineUseCase getStockByMedicineUseCase,
            GetStockAveragesByHospitalUseCase getStockAveragesByHospitalUseCase,
            GetStockReportByHospitalUseCase getStockReportByHospitalUseCase,
            GetMonthlyReportsUseCase getMonthlyReportsUseCase) {
        this.getStockByMedicineUseCase = getStockByMedicineUseCase;
        this.getStockAveragesByHospitalUseCase = getStockAveragesByHospitalUseCase;
        this.getStockReportByHospitalUseCase = getStockReportByHospitalUseCase;
        this.getMonthlyReportsUseCase = getMonthlyReportsUseCase;
    }

    @Path("/stock")
    @GET
    @PermitPublic
    @Operation(
            summary = "Get hospital stock by medicine name",
            description = "Returns stock availability of a medicine across all hospitals. No authentication required.")
    @Parameter(name = "medicine_name", description = "Generic name of the medicine to look up", required = true)
    @APIResponse(
            responseCode = "200",
            description = "List of hospitals with stock information for the requested medicine",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = MedicinesHospitalsStockDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value = "[{\"hospitalId\": 1, \"hospitalName\": \"Hospital Civil\","
                                                    + " \"address\": \"Calle 5 #10\", \"stockLabel\": \"Alto\","
                                                    + " \"status\": \"Disponible\", \"mapsUrl\":"
                                                    + " \"https://maps.google.com/?q=...\"}]")))
    @APIResponse(
            responseCode = "400",
            description = "medicine_name query parameter is missing or blank",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples = @ExampleObject(value = "{\"error\": \"medicine_name is required\"}")))
    public Response getByMedicine(@QueryParam("medicine_name") String medicineName) {
        if (medicineName == null || medicineName.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"medicine_name is required\"}")
                    .build();
        }
        List<MedicinesHospitalsStockDto> result = getStockByMedicineUseCase.execute(medicineName);
        return Response.ok(result).build();
    }

    @Path("/{idHospital}/average-stock")
    @GET
    @RequireRoles({"health"})
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Get average stock by hospital",
            description =
                    "Returns the average medicine stock for the last month and the current month for a given hospital."
                            + " Requires health role.")
    @Parameter(name = "idHospital", description = "Hospital identifier", required = true)
    @APIResponse(
            responseCode = "200",
            description = "Average stock figures for the hospital",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value = "{\"last_month_avg\": 142.50, \"current_month_avg\": 98.75}")))
    @APIResponse(
            responseCode = "400",
            description = "idHospital path parameter is missing",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples = @ExampleObject(value = "{\"error\": \"idHospital is required\"}")))
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the health role")
    public Response getAvgStock(@PathParam("idHospital") Integer idHospital, @Valid @BeanParam StockAveragesDto req) {
        if (idHospital == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ERROR_ID_HOSPITAL_REQUIRED)
                    .build();
        }

        Optional<StockAveragesResponse> averages = getStockAveragesByHospitalUseCase.execute(idHospital, req);
        return Response.ok(averages).build();
    }

    @Path("/{idHospital}/stock-report")
    @GET
    @RequireRoles({"health"})
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Get stock report by hospital",
            description = "Returns the number of medicines with low stock and the names of the most critical ones for a"
                    + " given hospital. Requires health role.")
    @Parameter(name = "idHospital", description = "Hospital identifier", required = true)
    @APIResponse(
            responseCode = "200",
            description = "Stock report for the hospital",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value = "{\"low_stock_count\": 5, \"bottom_medicines\": [\"Paracetamol\","
                                                    + " \"Ibuprofeno\", \"Amoxicilina\"]}")))
    @APIResponse(
            responseCode = "400",
            description = "idHospital path parameter is missing",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples = @ExampleObject(value = "{\"error\": \"idHospital is required\"}")))
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the health role")
    public Response getStockReport(@PathParam("idHospital") Integer idHospital, @Valid @BeanParam StockReportDto req) {
        if (idHospital == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ERROR_ID_HOSPITAL_REQUIRED)
                    .build();
        }

        Optional<StockReportResponse> stockReport = getStockReportByHospitalUseCase.execute(idHospital, req);
        return Response.ok(stockReport).build();
    }

    @Path("/{idHospital}/monthly-reports")
    @GET
    @RequireRoles({"health"})
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Get monthly reports by hospital",
            description = "Returns the report count for the current month and a comparison percentage to the previous"
                    + " month for a given hospital. Requires health role.")
    @Parameter(name = "idHospital", description = "Hospital identifier", required = true)
    @Parameter(name = "first_date", description = "Start date of the period (yyyy-MM-dd)", required = true)
    @Parameter(name = "second_date", description = "End date of the period (yyyy-MM-dd)", required = true)
    @APIResponse(
            responseCode = "200",
            description = "Monthly report data for the hospital",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples =
                                    @ExampleObject(
                                            name = "sample",
                                            value = "{\"current_month_report_count\": 12,"
                                                    + " \"comparison_to_last_month\": -15.50}")))
    @APIResponse(
            responseCode = "400",
            description = "idHospital, first_date, or second_date is missing",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples = @ExampleObject(value = "{\"error\": \"idHospital is required\"}")))
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the health role")
    public Response getMonthlyReports(
            @PathParam("idHospital") Integer idHospital, @Valid @BeanParam MonthlyReportsDto req) {
        if (idHospital == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ERROR_ID_HOSPITAL_REQUIRED)
                    .build();
        }

        Optional<MonthlyReportsResponse> monthlyReports = getMonthlyReportsUseCase.execute(idHospital, req);
        return Response.ok(monthlyReports).build();
    }
}
