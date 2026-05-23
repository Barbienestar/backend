package com.itesm.application.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Summary of a report as seen by its author")
public class ReportSummaryDto {

    @Schema(description = "Report identifier")
    private Long id;

    @JsonProperty("medicine_name")
    @Schema(description = "Generic name of the reported medicine")
    private String medicineName;

    @JsonProperty("hospital_name")
    @Schema(description = "Name of the hospital where the shortage was observed")
    private String hospitalName;

    @Schema(description = "Current status label (e.g. Pendiente, En proceso, Resuelto)")
    private String status;

    @Schema(description = "Detailed description provided by the citizen")
    private String description;

    @JsonProperty("image_url")
    @Schema(description = "URL of the supporting image")
    private String imageUrl;

    @JsonProperty("created_at")
    @Schema(description = "Timestamp when the report was created (ISO-8601)")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @Schema(description = "Timestamp of the last status update (ISO-8601)")
    private LocalDateTime updatedAt;
}
