package com.itesm.interfaces.rest;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.application.dto.UpdateUserDto;
import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.usecase.CreateUserUseCase;
import com.itesm.application.usecase.UpdateUserProfileUseCase;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Users", description = "User registration and management")
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
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account. No authentication required. "
                    + "Provide hospital_ids when registering a user with the health role.")
    @RequestBody(
            description = "User registration data",
            required = true,
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = CreateUserDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "citizen",
                                            value =
                                                    "{\"name\": \"Ana\", \"last_name_1\": \"García\", \"last_name_2\": \"López\", "
                                                            + "\"age\": 30, \"email\": \"ana@example.com\", \"password\": \"secret123\", "
                                                            + "\"role_id\": 1, \"suburb_id\": 100, \"hospital_ids\": []}")))
    @APIResponse(
            responseCode = "200",
            description = "User created successfully — returns the new user profile",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserProfileDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "created",
                                            value =
                                                    "{\"id\": 5, \"name\": \"Ana\", \"last_name_1\": \"García\", \"role\": \"citizen\", \"email\": \"ana@example.com\"}")))
    public Response createUser(@Valid CreateUserDto createUserDto) {
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
