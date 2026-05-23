package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Payload for creating a new citizen report")
public class CreateReportDto {

    @NotNull(message = "Description is required")
    @JsonProperty("medicine_id")
    @Schema(description = "Identifier of the medicine being reported", required = true)
    private Integer medicineId;

    @NotNull(message = "Description is required")
    @JsonProperty("hospital_id")
    @Schema(description = "Identifier of the hospital where the shortage was observed", required = true)
    private Integer hospitalId;

    @NotBlank(message = "Description is required")
    @Schema(description = "Detailed description of the shortage or issue", required = true)
    private String description;

    @NotBlank(message = "Image URL is required")
    @JsonProperty("image_url")
    @Schema(description = "Public URL of the supporting image (obtained from POST /image/upload)", required = true)
    private String imageUrl;
}
