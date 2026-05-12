package com.itesm.application.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private Long id;

    @JsonProperty("medicine_name")
    private String medicineName;

    @JsonProperty("hospital_name")
    private String hospitalName;

    @JsonProperty("status_id")
    private Byte statusId;

    private String description;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}