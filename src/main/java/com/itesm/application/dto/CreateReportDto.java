package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReportDto {
    @NotNull(message = "Description is required")
    @JsonProperty("medicine_id")
    private Integer medicineId;

    @NotNull(message = "Description is required")
    @JsonProperty("hospital_id")
    private Integer hospitalId;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Image URL is required")
    @JsonProperty("image_url")
    private String imageUrl;
}
