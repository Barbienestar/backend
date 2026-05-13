package com.itesm.interfaces.rest;

import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.UploadReportImageUseCase;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Path("/image")
public class ImageUploadResource {
    private final UploadReportImageUseCase uploadReportImageUseCase;

    @Inject
    public ImageUploadResource(UploadReportImageUseCase uploadReportImageUseCase) {
        this.uploadReportImageUseCase = uploadReportImageUseCase;
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RequireRoles({"admin"})
    public Response upload(@RestForm("image") FileUpload file) {
        if (file == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("No file uploaded").build();
        }

        try {
            byte[] bytes = Files.readAllBytes(file.uploadedFile());

            String url =
                    uploadReportImageUseCase.execute(bytes, file.fileName(), file.contentType());

            return Response.ok(Map.of("imageUrl", url)).build();

        } catch (IOException e) {
            return Response.serverError().entity("Failed to read file: " + e.getMessage()).build();
        }
    }
}
