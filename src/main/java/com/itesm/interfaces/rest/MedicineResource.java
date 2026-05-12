package com.itesm.interfaces.rest;

import com.itesm.application.dto.MedicineDto;
import com.itesm.application.dto.MedicineRowDto;
import com.itesm.application.dto.MedicineStockInputDto;
import com.itesm.application.dto.MedicineStockResultDto;
import com.itesm.application.usecase.GetMedicinesUseCase;
import com.itesm.application.usecase.UploadMedicineStockUseCase;
import com.itesm.infrastructure.csv.CsvParser;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.InputStream;
import java.util.List;

import org.jboss.resteasy.reactive.RestForm;

@Path("/medicines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicineResource {

    private final GetMedicinesUseCase getMedicinesUseCase;
    private final UploadMedicineStockUseCase uploadMedicineStockUseCase;


    @Inject
    public MedicineResource(GetMedicinesUseCase getMedicinesUseCase, UploadMedicineStockUseCase uploadMedicineStockUseCase) {
        this.getMedicinesUseCase = getMedicinesUseCase;
        this.uploadMedicineStockUseCase = uploadMedicineStockUseCase;
    }

    @GET
    public Response getAll() {
        List<MedicineDto> medicines = getMedicinesUseCase.execute();
        return Response.ok(medicines).build();
    }


    @POST
    @Path("/upload-stock/{idHospital}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadStock(@PathParam("idHospital") Integer idHospital,
                                @RestForm("file") InputStream file) {

        List<MedicineRowDto> rows = CsvParser.parse(file);
        MedicineStockInputDto input = new MedicineStockInputDto(idHospital, rows);
        MedicineStockResultDto result = uploadMedicineStockUseCase.execute(input);

        return Response.ok(result).build();
    }
}