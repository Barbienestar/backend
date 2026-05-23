package com.itesm.interfaces.rest;

import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.UploadReportImageUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Tag(name = "Images", description = "Report image uploads")
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
    @RequireRoles({"citizen"})
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
            summary = "Upload a report image",
            description = "Uploads an image file to be associated with a report. "
                    + "Accepts multipart/form-data with a field named 'image'. Requires citizen role.")
    @RequestBody(
            description = "Image file to upload (JPEG, PNG, etc.)",
            required = true,
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA))
    @APIResponse(
            responseCode = "200",
            description = "Image uploaded successfully — returns the public URL",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            examples =
                                    @ExampleObject(
                                            name = "success",
                                            value =
                                                    "{\"imageUrl\": \"https://storage.example.com/reports/abc123.jpg\"}")))
    @APIResponse(responseCode = "400", description = "No file provided in the request")
    @APIResponse(responseCode = "401", description = "Missing or invalid Bearer token")
    @APIResponse(responseCode = "403", description = "Authenticated user does not have the citizen role")
    @APIResponse(responseCode = "500", description = "Failed to read or upload the file")
    public Response upload(@RestForm("image") FileUpload file) {
        if (file == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("No file uploaded")
                    .build();
        }

        try {
            byte[] bytes = Files.readAllBytes(file.uploadedFile());

            String url = uploadReportImageUseCase.execute(bytes, file.fileName(), file.contentType());

            return Response.ok(Map.of("imageUrl", url)).build();

        } catch (IOException e) {
            return Response.serverError()
                    .entity("Failed to read file: " + e.getMessage())
                    .build();
        }
    }
}
