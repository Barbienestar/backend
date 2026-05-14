package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/** FullReportResponse */
@Data
@AllArgsConstructor
public class FullReportResponse {
    private Long id;
    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("user_full_name")
    private String userFullName;

    @JsonProperty("medicine_name")
    private String medicineName;

    @JsonProperty("medicine_presentation")
    private String medicinePresentation;

    @JsonProperty("medicine_dosage_form")
    private String medicineDosageForm;

    @JsonProperty("hospital_name")
    private String hospitalName;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
