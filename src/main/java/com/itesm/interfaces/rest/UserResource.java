package com.itesm.interfaces.rest;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.application.dto.UpdateUserDto;
import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.usecase.CreateUserUseCase;
import com.itesm.application.usecase.UpdateUserProfileUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final CreateUserUseCase createUserUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;

    @Inject
    public UserResource(CreateUserUseCase createUserUseCase, UpdateUserProfileUseCase updateUserProfileUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.updateUserProfileUseCase = updateUserProfileUseCase;
    }

    @POST
    @PermitPublic
    public Response createUser(CreateUserDto createUserDto) {
        UserProfileDto user = createUserUseCase.execute(createUserDto);
        return Response.ok(user).build();
    }

    @PATCH
    public Response updateUser(UpdateUserDto updateUserDto) {
        UserProfileDto user = updateUserProfileUseCase.execute(updateUserDto);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }
}
