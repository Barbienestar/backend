package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Report created by a citizen")
public class ReportDto {

    @Schema(description = "Report identifier")
    private Long id;

    @JsonProperty("medicine_name")
    @Schema(description = "Generic name of the reported medicine")
    private String medicineName;

    @JsonProperty("hospital_name")
    @Schema(description = "Name of the hospital where the shortage was observed")
    private String hospitalName;

    @JsonProperty("status_id")
    @Schema(description = "Current status identifier of the report")
    private Byte statusId;

    @Schema(description = "Detailed description provided by the citizen")
    private String description;

    @JsonProperty("created_at")
    @Schema(description = "Timestamp when the report was created (ISO-8601)")
    private LocalDateTime createdAt;
}
