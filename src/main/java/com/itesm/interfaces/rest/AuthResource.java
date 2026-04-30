package com.itesm.interfaces.rest;

import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.RequireRoles;
import com.itesm.application.usecase.GetUserProfileUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.Map;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    private final GetUserProfileUseCase getUserProfileUseCase;

    @Inject
    public AuthResource(GetUserProfileUseCase getUserProfileUseCase) {
        this.getUserProfileUseCase = getUserProfileUseCase;
    }

    @Path("/me")
    @GET
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
