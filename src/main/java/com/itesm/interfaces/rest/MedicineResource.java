package com.itesm.interfaces.rest;

import com.itesm.application.dto.MedicineDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.usecase.GetMedicinesUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/medicines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicineResource {

    private final GetMedicinesUseCase getMedicinesUseCase;

    @Inject
    public MedicineResource(GetMedicinesUseCase getMedicinesUseCase) {
        this.getMedicinesUseCase = getMedicinesUseCase;
    }

    @GET
    @PermitPublic
    public Response getAll(@QueryParam("q") String q) {
        List<MedicineDto> medicines = (q != null && !q.isBlank())
                ? getMedicinesUseCase.search(q)
                : getMedicinesUseCase.execute();
        return Response.ok(medicines).build();
    }
}