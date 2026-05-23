package com.itesm.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicinesHospitalsStockReport {
    @JsonProperty("low_stock_count")
    private Integer lowStockCount;

    @JsonProperty("bottom_medicines")
    private List<String> bottomMedicines;
}
