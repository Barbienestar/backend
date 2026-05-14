package com.itesm.application.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportSummaryDto {
    private Long id;

    @JsonProperty("medicine_name")
    private String medicineName;

    @JsonProperty("hospital_name")
    private String hospitalName;

    private String status;

    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
