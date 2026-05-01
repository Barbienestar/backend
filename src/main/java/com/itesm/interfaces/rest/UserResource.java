package com.itesm.interfaces.rest;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.application.usecase.CreateUserUseCase;
import com.itesm.domain.models.User;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/** UserResource */
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private CreateUserUseCase createUserUseCase;

    @Inject
    public UserResource(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @POST
    public Response createUser(CreateUserDto createUserDto) {
        User user = createUserUseCase.execute(createUserDto);
        return Response.ok(user).build();
    }
}
