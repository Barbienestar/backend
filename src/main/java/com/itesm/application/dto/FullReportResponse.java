package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

/** FullReportResponse */
@Data
@AllArgsConstructor
@Schema(description = "Full report detail as seen by an admin")
public class FullReportResponse {

    @Schema(description = "Report identifier")
    private Long id;

    @Schema(description = "Detailed description provided by the citizen")
    private String description;

    @JsonProperty("image_url")
    @Schema(description = "URL of the supporting image")
    private String imageUrl;

    @JsonProperty("user_full_name")
    @Schema(description = "Full name of the citizen who submitted the report")
    private String userFullName;

    @JsonProperty("medicine_name")
    @Schema(description = "Generic name of the reported medicine")
    private String medicineName;

    @JsonProperty("medicine_presentation")
    @Schema(description = "Presentation / pack size of the medicine")
    private String medicinePresentation;

    @JsonProperty("medicine_dosage_form")
    @Schema(description = "Dosage form of the medicine (e.g. tablet, solution)")
    private String medicineDosageForm;

    @JsonProperty("hospital_name")
    @Schema(description = "Name of the hospital where the shortage was observed")
    private String hospitalName;

    @JsonProperty("created_at")
    @Schema(description = "Timestamp when the report was created (ISO-8601)")
    private LocalDateTime createdAt;
}
