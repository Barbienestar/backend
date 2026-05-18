package com.itesm.interfaces.rest;

import com.itesm.application.dto.MedicineDto;
import com.itesm.application.dto.MedicineRowDto;
import com.itesm.application.dto.MedicineStockInputDto;
import com.itesm.application.dto.MedicineStockResultDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.usecase.GetMedicinesUseCase;
import com.itesm.application.usecase.UploadMedicineStockUseCase;
import com.itesm.infrastructure.csv.CsvParser;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

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
    @PermitPublic
    public Response getAll(@QueryParam("q") String q) {
        List<MedicineDto> medicines = (q != null && !q.isBlank())
                ? getMedicinesUseCase.search(q)
                : getMedicinesUseCase.execute();
        return Response.ok(medicines).build();
    }


    @POST
    @Path("/upload-stock/{idHospital}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadStock(@PathParam("idHospital") Integer idHospital,
                                @RestForm("file") FileUpload file) throws IOException {

        if (file == null || file.size() == 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El archivo está vacío o no fue enviado").build();
        }

        String contentType = file.contentType();
        if (contentType == null || (!contentType.equals("text/csv")
                && !contentType.equals("application/csv")
                && !contentType.equals("text/plain"))) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El archivo debe ser un CSV (text/csv)").build();
        }

        try (InputStream inputStream = Files.newInputStream(file.uploadedFile())) {
            List<MedicineRowDto> rows = CsvParser.parse(inputStream);
            MedicineStockInputDto input = new MedicineStockInputDto(idHospital, rows);
            MedicineStockResultDto result = uploadMedicineStockUseCase.execute(input);
            return Response.ok(result).build();
        }
    }
}