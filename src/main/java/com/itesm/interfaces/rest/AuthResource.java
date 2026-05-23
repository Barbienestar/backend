package com.itesm.interfaces.rest;

import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.usecase.GetUserProfileUseCase;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Map;

@Tag(name = "Auth", description = "Authentication and user profile operations")
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final GetUserProfileUseCase getUserProfileUseCase;

    @Inject
    public AuthResource(GetUserProfileUseCase getUserProfileUseCase) {
        this.getUserProfileUseCase = getUserProfileUseCase;
    }

    @GET
    @Path("/me")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(
        summary = "Get current user profile",
        description = "Returns the profile of the authenticated user. Requires a valid Bearer token."
    )
    @APIResponse(
        responseCode = "200",
        description = "User profile retrieved successfully",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(implementation = UserProfileDto.class),
            examples = @ExampleObject(
                name = "citizen",
                value = "{\"id\": 1, \"name\": \"Ana\", \"last_name_1\": \"García\", \"role\": \"citizen\", \"email\": \"ana.garcia@example.com\"}"
            )
        )
    )
    @APIResponse(
        responseCode = "401",
        description = "Missing or invalid Bearer token",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON,
            examples = @ExampleObject(
                name = "unauthorized",
                value = "{\"error\": \"User not found\"}"
            )
        )
    )
    public Response getUserProfile() {
        UserProfileDto userProfile = getUserProfileUseCase.execute();
        if (userProfile == null) {
            return Response.serverError()
                    .entity(Map.of("error", "User not found"))
                    .status(Status.UNAUTHORIZED)
                    .build();
        }
        return Response.ok(userProfile).build();
    }
}
