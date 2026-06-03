package com.itesm.interfaces.rest;

import com.itesm.application.dto.CreateUserDto;
import com.itesm.application.dto.UpdateUserDto;
import com.itesm.application.dto.UserProfileDto;
import com.itesm.application.security.PermitPublic;
import com.itesm.application.security.RequireRoles;
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
    @Path("/citizen")
    @PermitPublic
    @Operation(summary = "Register a new user", description = "Creates a new user account. No authentication required.")
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
                                            value = "{\"name\": \"Ana\", \"last_name_1\": \"García\", \"last_name_2\":"
                                                    + " \"López\", \"age\": 30, \"email\": \"ana@example.com\","
                                                    + " \"password\": \"secret123\", \"role_id\": 1,"
                                                    + " \"suburb_id\": 100, \"hospital_ids\": []}")))
    @APIResponse(
            responseCode = "201",
            description = "User created successfully — returns the new user profile",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserProfileDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "created",
                                            value = "{\"id\": 5, \"name\": \"Ana\", \"last_name_1\": \"García\","
                                                    + " \"role\": \"citizen\", \"email\": \"ana@example.com\"}")))
    public Response createCitizenUser(@Valid CreateUserDto createUserDto) {
        return createdUserResponse(createUserDto);
    }

    @POST
    @Path("/privileged")
    @RequireRoles({"admin"})
    @Operation(
            summary = "Register a new privileged user",
            description = "Creates a new privileged User account. Admin role required. "
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
                                            name = "admin",
                                            value = "{\"name\": \"Ana\", \"last_name_1\": \"García\", \"last_name_2\":"
                                                    + " \"López\", \"email\": \"ana@example.com\","
                                                    + " \"password\": \"secret123\", \"role_id\": 1,")))
    @APIResponse(
            responseCode = "201",
            description = "User created successfully — returns the new user profile",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserProfileDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "created",
                                            value = "{\"id\": 5, \"name\": \"Ana\", \"last_name_1\": \"García\","
                                                    + " \"role\": \"admin\", \"email\": \"ana@example.com\"}")))
    public Response createPrivilegedUser(@Valid CreateUserDto createUserDto) {
        return createdUserResponse(createUserDto);
    }

    private Response createdUserResponse(CreateUserDto dto) {
        return Response.status(Response.Status.CREATED).entity(createUserUseCase.execute(dto)).build();
    }

    @PATCH
    @Operation(
            summary = "Update own profile",
            description = "Updates the authenticated user's profile fields. Only the provided fields are updated.")
    @RequestBody(
            description = "Profile fields to update (partial update — all fields optional)",
            required = true,
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UpdateUserDto.class),
                            examples =
                                    @ExampleObject(
                                            name = "update",
                                            value = "{\"name\": \"Ana\", \"last_name_1\": \"Martínez\", \"age\": 31,"
                                                    + " \"suburb_id\": 200}")))
    @APIResponse(
            responseCode = "200",
            description = "Profile updated successfully",
            content =
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UserProfileDto.class)))
    @APIResponse(responseCode = "404", description = "User not found")
    public Response updateUser(UpdateUserDto updateUserDto) {
        UserProfileDto user = updateUserProfileUseCase.execute(updateUserDto);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }
}
