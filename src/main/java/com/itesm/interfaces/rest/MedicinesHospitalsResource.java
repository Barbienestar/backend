package com.itesm.interfaces.rest;

import java.util.List;
import java.util.Optional;

import org.jboss.resteasy.reactive.RestQuery;

import com.itesm.application.dto.MedicineHospitalStockDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.usecase.GetStockAveragesByHospitalUseCase;
import com.itesm.application.usecase.GetStockByMedicineUseCase;
import com.itesm.domain.models.MedicinesHospitalsStockAverages;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/medicines-hospitals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicinesHospitalsResource {
    private final GetStockByMedicineUseCase getStockByMedicineUseCase;
    private final GetStockAveragesByHospitalUseCase getStockAveragesByHospitalUseCase;

    @Inject
    public MedicinesHospitalsResource(
        GetStockByMedicineUseCase getStockByMedicineUseCase, 
        GetStockAveragesByHospitalUseCase getStockAveragesByHospitalUseCase
    ) {
        this.getStockByMedicineUseCase = getStockByMedicineUseCase;
        this.getStockAveragesByHospitalUseCase = getStockAveragesByHospitalUseCase;
    }

    @Path("/stock")
    @GET
    @PermitPublic
    public Response getByMedicine(@QueryParam("medicine_name") String medicineName) {
        if (medicineName == null || medicineName.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"medicine_name is required\"}")
                    .build();
        }
        List<MedicineHospitalStockDto> result = getStockByMedicineUseCase.execute(medicineName);
        return Response.ok(result).build();
    }

    @Path("/average-stock")
    @GET
    @PermitPublic
    public Response getAvgStock(@RestQuery Integer id_hospital) {
        if (id_hospital == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"id_hospital is required\"}")
                    .build();
        }

        Optional<MedicinesHospitalsStockAverages> averages = getStockAveragesByHospitalUseCase.execute(id_hospital);
        return Response.ok(averages).build();
    }
}
