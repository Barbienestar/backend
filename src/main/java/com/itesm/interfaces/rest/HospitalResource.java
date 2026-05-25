package com.itesm.interfaces.rest;

import com.itesm.application.dto.HospitalCriticalMedicinesDto;
import com.itesm.application.dto.HospitalDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.GetCriticalMedicinesUseCase;
import com.itesm.application.usecase.GetHospitalsUseCase;
import com.itesm.application.usecase.GetMyHospitalsUseCase;
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
    private final GetMyHospitalsUseCase getMyHospitalsUseCase;
    private final GetCriticalMedicinesUseCase getCriticalMedicinesUseCase;

    @Inject
    public HospitalResource(
            GetHospitalsUseCase getHospitalsUseCase,
            GetMyHospitalsUseCase getMyHospitalsUseCase,
            GetCriticalMedicinesUseCase getCriticalMedicinesUseCase) {
        this.getHospitalsUseCase = getHospitalsUseCase;
        this.getMyHospitalsUseCase = getMyHospitalsUseCase;
        this.getCriticalMedicinesUseCase = getCriticalMedicinesUseCase;
    }

    @GET
    @PermitPublic
    public Response getAll() {
        List<HospitalDto> hospitals = getHospitalsUseCase.execute();
        return Response.ok(hospitals).build();
    }

    @GET
    @Path("/my-hospitals")
    public Response getMyHospitals() {
        List<HospitalDto> hospitals = getMyHospitalsUseCase.execute();
        return Response.ok(hospitals).build();
    }

    @GET
    @Path("/critical-medicines")
    @RequireRoles({"health"})
    public Response getCriticalMedicines() {
        List<HospitalCriticalMedicinesDto> result = getCriticalMedicinesUseCase.execute();
        return Response.ok(result).build();
    }
}
