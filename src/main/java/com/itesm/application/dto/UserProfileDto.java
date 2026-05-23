package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@Schema(description = "Authenticated user profile")
public class UserProfileDto {

    @Schema(description = "Unique user identifier")
    private Long id;

    @Schema(description = "First name")
    private String name;

    @JsonProperty("last_name_1")
    @Schema(description = "First surname")
    private String lastName1;

    @Schema(description = "Assigned role: citizen, health or admin")
    private String role;

    @Schema(description = "Email address")
    private String email;
}
