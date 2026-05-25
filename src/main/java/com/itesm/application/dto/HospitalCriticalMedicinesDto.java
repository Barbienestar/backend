package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HospitalCriticalMedicinesDto {
    @JsonProperty("hospital_id")
    private Integer hospitalId;

    @JsonProperty("hospital_name")
    private String hospitalName;

    @JsonProperty("critical_medicines")
    private List<CriticalMedicineDto> criticalMedicines;
}
