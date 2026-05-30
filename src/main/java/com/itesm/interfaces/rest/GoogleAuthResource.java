package com.itesm.interfaces.rest;

import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.usecase.GoogleSignInUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Auth", description = "Authentication and user profile operations")
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GoogleAuthResource {

    private final GoogleSignInUseCase googleSignInUseCase;

    @Inject
    public GoogleAuthResource(GoogleSignInUseCase googleSignInUseCase) {
        this.googleSignInUseCase = googleSignInUseCase;
    }

    @GET
    @Path("/google")
    @PermitPublic
    @Operation(
            summary = "Sign in with Google",
            description = "Accepts a Firebase Google ID token in the Authorization header. Auto-provisions a citizen"
                    + " account on first use. Rejects non-citizen roles with 403.")
    @APIResponse(
            responseCode = "200",
            description = "User profile returned (newly created or existing)",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserProfileDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "citizen",
                                            value = "{\"id\": 10, \"name\": \"Ana\", \"last_name_1\": \"García\","
                                                    + " \"role\": \"citizen\", \"email\": \"ana@gmail.com\"}")))
    @APIResponse(responseCode = "401", description = "Missing or invalid Google token")
    @APIResponse(responseCode = "403", description = "Account is not a citizen")
    public Response googleSignIn(@HeaderParam("Authorization") String authHeader) {
        UserProfileDto userProfile = googleSignInUseCase.execute(authHeader);
        return Response.ok(userProfile).build();
    }
}
