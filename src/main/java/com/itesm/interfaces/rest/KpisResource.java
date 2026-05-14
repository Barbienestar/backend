package com.itesm.interfaces.rest;

import java.util.Optional;

import org.jboss.resteasy.reactive.RestQuery;

import com.itesm.application.security.PermitPublic;
import com.itesm.application.usecase.GetStockAveragesByHospitalUseCase;
import com.itesm.domain.models.MedicinesHospitalsStockAverages;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/kpis")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KpisResource {
    private final GetStockAveragesByHospitalUseCase getStockAveragesByHospitalUseCase;

    @Inject
    public KpisResource(GetStockAveragesByHospitalUseCase getStockAveragesByHospitalUseCase) {
        this.getStockAveragesByHospitalUseCase = getStockAveragesByHospitalUseCase;
    }

    @Path("/averages")
    @GET
    @PermitPublic
    public Response getAvgStock(@RestQuery Integer id_hospital) {
        Optional<MedicinesHospitalsStockAverages> averages = getStockAveragesByHospitalUseCase.execute(id_hospital);
        return Response.ok(averages).build();
    }
}
