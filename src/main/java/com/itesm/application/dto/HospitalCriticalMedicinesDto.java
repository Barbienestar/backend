package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Hospital with at least one medicine whose stock is at or below the critical threshold")
public class HospitalCriticalMedicinesDto {

    @JsonProperty("hospital_id")
    @Schema(description = "Hospital identifier")
    private Integer hospitalId;

    @JsonProperty("hospital_name")
    @Schema(description = "Name of the hospital")
    private String hospitalName;

    @JsonProperty("critical_medicines")
    @Schema(description = "Medicines with critical stock levels, ordered by stock ASC")
    private List<CriticalMedicineDto> criticalMedicines;
}
