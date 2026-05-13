package com.itesm.interfaces.rest;

import com.itesm.application.dto.MedicineHospitalStockDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.usecase.GetStockByMedicineUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/stock")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StockResource {

    private final GetStockByMedicineUseCase getStockByMedicineUseCase;

    @Inject
    public StockResource(GetStockByMedicineUseCase getStockByMedicineUseCase) {
        this.getStockByMedicineUseCase = getStockByMedicineUseCase;
    }

    @GET
    @PermitPublic
    public Response getByMedicine(@QueryParam("medicineName") String medicineName) {
        if (medicineName == null || medicineName.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"medicineName is required\"}")
                    .build();
        }
        List<MedicineHospitalStockDto> result = getStockByMedicineUseCase.execute(medicineName);
        return Response.ok(result).build();
    }
}