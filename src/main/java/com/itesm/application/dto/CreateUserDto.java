package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Payload for registering a new user")
public class CreateUserDto {

    @Schema(description = "First name", required = true)
    private String name;

    @JsonProperty("last_name_1")
    @Schema(description = "First surname", required = true)
    private String lastName1;

    @JsonProperty("last_name_2")
    @Schema(description = "Second surname")
    private String lastName2;

    @Schema(description = "Age in years", required = true)
    private Integer age;

    @Schema(description = "Email address used for login", required = true)
    private String email;

    @Schema(description = "Plain-text password (hashed server-side)", required = true)
    private String password;

    @JsonProperty("role_id")
    @Schema(description = "Role identifier (1 = citizen, 2 = health, 3 = admin)", required = true)
    private Byte roleId;

    @JsonProperty("suburb_id")
    @Schema(description = "Suburb identifier where the user resides", required = true)
    private Integer suburbId;

    @JsonProperty("hospital_ids")
    @Schema(description = "Hospital identifiers to associate with the user (required for health role)")
    private List<Integer> hospitalIds;
}
