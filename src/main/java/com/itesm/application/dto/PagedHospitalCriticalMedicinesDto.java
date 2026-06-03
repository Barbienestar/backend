package com.itesm.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagedHospitalCriticalMedicinesDto {
    @JsonProperty("hospital_id")
    private Integer hospitalId;

    @JsonProperty("hospital_name")
    private String hospitalName;

    @JsonProperty("critical_medicines")
    private List<CriticalMedicineDto> criticalMedicines;

    @JsonProperty("total_elements")
    private long totalElements;

    @JsonProperty("total_pages")
    private int totalPages;

    @JsonProperty("current_page")
    private int currentPage;
}
