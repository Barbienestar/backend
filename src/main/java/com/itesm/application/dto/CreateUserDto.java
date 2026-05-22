package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserDto {
    @NotBlank(message = "Name is requiered")
    @Size(min = 1, max = 128, message = "Name must be between 1 and 128 characters")
    private String name;

    @JsonProperty("last_name_1")
    @NotBlank(message = "First last name is required")
    @Size(min = 1, max = 64, message = "First last name must be between 1 and 64 characters")
    private String lastName1;

    @JsonProperty("last_name_2")
    @Size(min = 1, max = 64, message = "Second last name must be between 1 and 64 characters")
    private String lastName2;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be at least 0")
    @Max(value = 255, message = "Age cannot exceed 255")
    private Byte age;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Password must have uppercase, lowercase and numbers")
    private String password;

    @JsonProperty("role_id")
    @NotNull(message = "Role ID is required")
    @Min(value = 1, message = "Role ID must be at least 1")
    @Max(value = 255, message = "Role ID cannot exceed 255")
    private Byte roleId;

    @JsonProperty("suburb_id")
    @NotNull(message = "Suburb ID is required")
    @Positive(message = "Suburb ID must be a valid positive number")
    private Integer suburbId;

    @JsonProperty("hospital_ids")
    private List<Integer> hospitalIds;
}
