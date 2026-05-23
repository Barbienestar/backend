package com.itesm.interfaces.rest;

import com.itesm.application.dto.MedicineDto;
import com.itesm.application.dto.MedicineRowDto;
import com.itesm.application.dto.MedicineStockInputDto;
import com.itesm.application.dto.MedicineStockResultDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.GetMedicinesUseCase;
import com.itesm.application.usecase.UploadMedicineStockUseCase;
import com.itesm.infrastructure.csv.CsvParser;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

@Tag(name = "Medicines", description = "Medicine catalog and hospital stock management")
@Path("/medicines")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicineResource {

    private final GetMedicinesUseCase getMedicinesUseCase;
    private final UploadMedicineStockUseCase uploadMedicineStockUseCase;

    @Inject
    public MedicineResource(
            GetMedicinesUseCase getMedicinesUseCase, UploadMedicineStockUseCase uploadMedicineStockUseCase) {
        this.getMedicinesUseCase = getMedicinesUseCase;
        this.uploadMedicineStockUseCase = uploadMedicineStockUseCase;
    }

    @GET
    @PermitPublic
    @Operation(
        summary = "List or search medicines",
        description = "Returns all medicines in the catalog. Pass the optional `q` query parameter to filter by name. No authentication required."
    )
    @Parameter(name = "q", description = "Search term to filter medicines by generic name (optional)")
    @APIResponse(
        responseCode = "200",
        description = "List of medicines",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = MedicineDto.class),
            examples = @ExampleObject(
                name = "sample",
                value = "[{\"id\": 1, \"generic_name\": \"Paracetamol\", \"dosage_form\": \"Tablet\", \"strength\": \"500 mg\", \"presentation\": \"20 tablets\"}]"
            )
        )
    )
    public Response getAll(@QueryParam("q") String q) {
        List<MedicineDto> medicines =
                (q != null && !q.isBlank()) ? getMedicinesUseCase.search(q) : getMedicinesUseCase.execute();
        return Response.ok(medicines).build();
    }

    @POST
    @Path("/upload-stock/{idHospital}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RequireRoles("health")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
        summary = "Upload medicine stock from CSV",
        description = "Processes a CSV file to update medicine stock levels for a given hospital. " +
                      "The file must have content-type text/csv and be sent in a field named `file`. Requires health role."
    )
    @Parameter(name = "idHospital", description = "Hospital identifier to update stock for", required = true)
    @RequestBody(
        description = "CSV file with medicine stock rows",
        required = true,
        content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA)
    )
    @APIResponse(
        responseCode = "200",
        description = "CSV processed — returns inserted count and any row-level errors",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = MedicineStockResultDto.class),
            examples = @ExampleObject(
                name = "partial",
                value = "{\"inserted\": 18, \"errors\": [\"Row 5: unknown medicine 'Aspirina X'\"]}"
            )
        )
    )
    @APIResponse(responseCode = "400", description = "Missing file or invalid content-type (must be text/csv)")
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the health role")
    public Response uploadStock(@PathParam("idHospital") Integer idHospital, @RestForm("file") FileUpload file)
            throws IOException {

        if (file == null || file.size() == 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El archivo está vacío o no fue enviado")
                    .build();
        }

        String contentType = file.contentType();
        if (contentType == null
                || (!contentType.equals("text/csv")
                        && !contentType.equals("application/csv")
                        && !contentType.equals("text/plain"))) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El archivo debe ser un CSV (text/csv)")
                    .build();
        }

        try (InputStream inputStream = Files.newInputStream(file.uploadedFile())) {
            List<MedicineRowDto> rows = CsvParser.parse(inputStream);
            MedicineStockInputDto input = new MedicineStockInputDto(idHospital, rows);
            MedicineStockResultDto result = uploadMedicineStockUseCase.execute(input);
            return Response.ok(result).build();
        }
    }
}
