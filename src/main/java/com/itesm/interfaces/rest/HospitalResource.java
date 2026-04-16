package com.itesm.interfaces.rest;

import com.itesm.application.dto.HospitalDto;
import com.itesm.application.usecase.GetHospitalsUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/hospitals")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HospitalResource {

    private final GetHospitalsUseCase getHospitalsUseCase;

    @Inject
    public HospitalResource(GetHospitalsUseCase getHospitalsUseCase) {
        this.getHospitalsUseCase = getHospitalsUseCase;
    }

    @GET
    public Response getAll() {
        List<HospitalDto> hospitals = getHospitalsUseCase.execute();
        return Response.ok(hospitals).build();
    }
}