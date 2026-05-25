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

    @JsonProperty("last_name1")
    @Schema(description = "First surname", name = "last_name1")
    private String lastName1;

    @JsonProperty("last_name2")
    @Schema(description = "Second surname", name = "last_name2")
    private String lastName2;

    @Schema(description = "User age")
    private Byte age;

    @Schema(description = "Assigned suburb or neighborhood")
    private SuburbDto suburb;

    @Schema(description = "Assigned role: citizen, health or admin")
    private String role;

    @Schema(description = "Email address")
    private String email;
}
