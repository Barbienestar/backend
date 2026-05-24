package com.itesm.interfaces.rest;

import com.itesm.application.dto.PeriodReportsByHospitalResponse;
import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.GetPeriodReportsByHospitalUseCase;
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

@Path("/reports-snapshots")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportsSnapshotResource {
    private final GetPeriodReportsByHospitalUseCase getPeriodReportsByHospitalUseCase;

    @Inject
    public ReportsSnapshotResource(GetPeriodReportsByHospitalUseCase getPeriodReportsByHospitalUseCase) {
        this.getPeriodReportsByHospitalUseCase = getPeriodReportsByHospitalUseCase;
    }

    @Path("/period/{hospital-id}")
    @GET
    @RequireRoles({"health"})
    public Response getPeriodReports(
            @PathParam("hospital-id") Integer idHospital,
            @QueryParam("start_date") LocalDate startDate,
            @QueryParam("end_date") LocalDate endDate) {
        if (idHospital == null || startDate == null || endDate == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"hospital-id, start-date, and end-date are required\"}")
                    .build();
        }
        List<PeriodReportsByHospitalResponse> result =
                getPeriodReportsByHospitalUseCase.execute(idHospital, startDate, endDate);
        return Response.ok(result).build();
    }
}
